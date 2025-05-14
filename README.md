# Tuiter Challenge

Desarrollada en Java 17 con Spring Boot. Soporta publicación de tuits, seguimiento de usuarios, visualización de timelines y emisión de eventos de dominio mediante Apache Kafka. Puede ejecutarse en modo desarrollo con base de datos H2, o bien con PostgreSQL y Kafka utilizando Docker. También puede empaquetarse en una imagen Docker lista para distribución o despliegue.

---

## Requisitos

- Java 17 o superior
- Docker y Docker Compose
- Bash (para ejecutar scripts `*.sh`)
- Gradle (o usar `./gradlew` incluido)

---

## Perfiles disponibles

| Perfil    | Descripción                               | Base de datos   | Kafka | Nota                                     |
|-----------|--------------------------------------------|-----------------|--------|------------------------------------------|
| `dev`     | Desarrollo rápido con H2 en memoria        | H2 embebido     | ✅     | Kafka debe levantarse con Docker aparte  |
| `local`   | Base PostgreSQL en contenedor Docker       | PostgreSQL      | ✅     | Kafka se levanta con Docker              |

---

## Modo desarrollo (`dev` con H2)

1. Ejecutar la aplicación con el perfil `dev`:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```

2. Asegurarse de levantar Kafka y postgres manualmente (ver sección [Kafka](#kafka)):

   ```bash
   docker compose up -d 
   ```

3. Los datos se reinician en cada ejecución (memoria volátil).

---

## Modo local con Docker (`local`)

1. Crear el archivo `.env` en el root del proyecto con el siguiente contenido:

   ```env
   POSTGRES_DB=tuiter
   POSTGRES_USER=admin
   POSTGRES_PASSWORD=admin123
   POSTGRES_PORT=5432
   SPRING_PROFILES_ACTIVE=local
   ```

2. Levantar los servicios:

   ```bash
   docker compose up -d
   ```

3. Ejecutar la aplicación con el perfil `local`:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=local'
   ```

---

## Empaquetado como imagen Docker

1. Ejecutar el script para compilar, construir la imagen y ejecutar:
   ```bash
   ./levantar-app.sh
   ```

2. Este script:
    - Apaga servicios previos
    - Levanta contenedores de PostgreSQL, Kafka y Zookeeper
    - Espera que estén listos
    - Compila el JAR
    - Construye la imagen Docker (`tuiter-app`)
    - Ejecuta el contenedor

3. Para limpiar volúmenes e imagen:
   ```bash
   ./limpiar.sh
   ```

---

## Kafka

Esta aplicación utiliza **Apache Kafka** para publicar eventos de dominio, como la publicación de tuits.

- El tópico principal es: `tuits-publicados`
- Los eventos se publican serializados en JSON
- Los consumidores deben confiar en el paquete: `com.challenge.tuiter.dominio.tuit.evento`

### Entornos y disponibilidad

| Entorno   | Kafka activo | Comentario                                                                          |
|-----------|--------------|--------------------------------------------------------------------------------------|
| `dev`     | ✅           | Usa H2, pero requiere que Kafka esté corriendo por fuera (`docker compose`).        |
| `local`   | ✅           | Kafka se levanta automáticamente con Docker.                                       |
| Docker    | ✅           | El script `levantar-app.sh` levanta Kafka, PostgreSQL y la app.                     |

### Configuración relevante (`application-*.properties`)

```properties
spring.kafka.bootstrap-servers=localhost:29092   # (en dev)
spring.kafka.bootstrap-servers=kafka:9092        # (en local)
spring.kafka.consumer.group-id=tuiter
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=com.challenge.tuiter.dominio.tuit.evento
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

---

## Ejecución de tests

### Requisitos

Para que todos los tests (unitarios e integración) se ejecuten correctamente, asegurate de tener:

- Solo se requiere tener Docker instalado y en ejecución. No es necesario levantar Redis, PostgreSQL ni Kafka manualmente.

> Ya están pre configurados en `docker-compose.override.yml`, por lo que solo necesitás:

```bash
docker compose up -d
```

### Tareas de test disponibles

| Tarea              | Descripción                                                                 |
|--------------------|------------------------------------------------------------------------------|
| `tests`            | Ejecuta todos los tests (unitarios e integración)                           |
| `testsUnitarios`   | Ejecuta solo los tests unitarios, excluyendo los que tengan `@Tag("integration")` |
| `testsIntegracion` | Ejecuta solo los tests de integración, es decir, los marcados con `@Tag("integration")` |

### Comandos

```bash
./gradlew tests              # Todos los tests
./gradlew testsUnitarios     # Solo tests unitarios
./gradlew testsIntegracion   # Solo tests de integración
```

### Tests de integración con Testcontainers

Los tests de integración utilizan **Testcontainers** para levantar entornos reales de Redis, PostgreSQL y Kafka de forma automática durante la ejecución de los tests.

#### Servicios levantados por Testcontainers:

| Servicio    | Imagen utilizada               | Puerto interno | Uso principal                                  |
|-------------|--------------------------------|----------------|------------------------------------------------|
| Redis       | redis:7-alpine                 | 6379           | Cache y timeline de usuarios                   |
| PostgreSQL  | postgres:15                    | 5432           | Persistencia de tuits y relaciones             |
| Kafka       | confluentinc/cp-kafka:7.5.0    | 9092           | Publicación de eventos de dominio (tuits)      |

#### Configuración

- Los contenedores se configuran en la clase base `BaseIntegrationTest`, que:
    - Expone los puertos dinámicos al entorno de Spring Boot.
    - Registra las propiedades dinámicamente con `@DynamicPropertySource`.
    - Desactiva la autoconfiguración de Redis (`@EnableAutoConfiguration(exclude = ...)`).
    - Usa una clase `RedisTestConfig` para registrar `RedisConnectionFactory` y `RedisTemplate` en base a los valores obtenidos desde los contenedores.

#### Estructura de los tests

- Los tests de integración extienden de `BaseIntegrationTest`.
- Se anotan con `@Tag("integration")` para permitir su ejecución selectiva.
- Usan `WebTestClient` para simular interacciones HTTP reales con la API.
- Se testean escenarios completos como:
    - Seguimiento entre usuarios.
    - Publicación de tuits.
    - Propagación del fan-out al timeline del seguidor.

#### Requisitos previos

> No necesitás levantar ningún servicio manualmente para ejecutar los tests de integración, ya que Testcontainers se encarga de iniciarlos.

#### Comando de ejecución (con logs visibles):

```bash
./gradlew testsIntegracion --info
```

---

## Endpoints disponibles

### Timeline
- `GET /usuarios/{usuarioId}/timeline`  
  Devuelve los tuits publicados por los usuarios que sigue `{usuarioId}`.

### Seguimientos
- `POST /seguimientos`  
  Publica una nueva relación de seguimiento. Body: `{ "seguidorId": "ana", "seguidoId": "pepe" }`

- `GET /seguimientos/{usuarioId}/seguidos`  
  Lista los usuarios seguidos por `{usuarioId}`.

- `GET /seguimientos/{usuarioId}/seguidores`  
  Lista los usuarios que siguen a `{usuarioId}`.

### Tuits
- `POST /tuits`  
  Publica un tuit.  
  Body:
  ```json
  {
    "autorId": "ana",
    "contenido": "Hola mundo"
  }
  ```

---

## Pruebas con Postman

1. En `postman/` se incluyen:

    - `Challenge-Tuiter.postman_collection.json`: colección completa
    - `Challenge-Tuiter.environment.json`: entorno con `base_url`
    - `tuits.csv`: datos de ejemplo

2. Para ejecutar el flujo:

    - Importar colección y entorno en Postman
    - Abrir el **Runner**
    - Seleccionar la colección y el folder **Flujo completo**
    - Cargar `tuits.csv` como archivo de datos
    - Ejecutar el flujo completo

3. El flujo:
    - `ana` sigue a `bruno`, `carla`, `dario`, `elena`
    - Se publican 10 tuits por cada uno desde el CSV
    - Se consulta el timeline y relaciones

---

## Estructura del proyecto

```
.
├── .env
├── docker-compose.yml
├── docker-compose.override.yml
├── Dockerfile
├── levantar-app.sh
├── limpiar.sh
├── postman/
│   ├── Challenge-Tuiter.postman_collection.json
│   ├── Challenge-Tuiter.environment.json
│   ├── tuits.csv
│   └── datos-generados.md
├── src/
│   ├── resources
│   │   └── logback-spring.xml
└── build.gradle
```

---

## Escalabilidad y Balanceo de Carga

La aplicación puede ejecutarse en múltiples instancias detrás de un balanceador de carga (por ejemplo, NGINX o AWS ELB). Para ello:

- Asegúrese de configurar `server.forward-headers-strategy=framework`.
- Existen endpoints de salud disponibles en `/actuator/health` para chequeos automáticos.

---

### Documentación adicional

En la [Wiki](https://github.com/mdAlvarenga/tuiter/wiki) se detallan aspectos técnicos del proyecto, incluyendo:

- Descripción de la arquitectura y sus componentes
- Diagramas de flujo y de infraestructura
- Estrategias de escalabilidad
- Guías para deploy y troubleshooting
- Supuestos de negocio