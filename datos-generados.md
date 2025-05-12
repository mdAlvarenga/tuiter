# Datos generados por la colección de Postman

Esta colección crea datos básicos para realizar pruebas sobre la API del sistema. Sirve para validar endpoints de publicación de tuits, seguimiento de usuarios y lectura de timelines.

---

## Usuarios creados

| Usuario | Descripción                   |
|--------:|-------------------------------|
| `ana`   | Usuario principal de prueba   |
| `bruno` | Usuario seguido por `ana`     |
| `carla` | Usuario seguido por `ana`     |
| `dario` | Usuario seguido por `ana`     |
| `elena` | Usuario seguido por `ana`     |

---

## Relaciones de seguimiento

- `ana` **sigue a**: `bruno`, `carla`, `dario`, `elena`
- `bruno`, `carla`, `dario`, `elena` **tienen como seguidora a** `ana`

---

## Tuits generados

Cada uno de los siguientes usuarios publica entre 1 y 10 tuits con contenido aleatorio:

- `bruno`
- `carla`
- `dario`
- `elena`

El contenido de los tuits es aleatorio y se puede consultar desde el timeline del usuario `ana`.

---

## Endpoints sugeridos para validar

### Timeline de `ana` (con tuits de sus seguidos)

    `GET /usuarios/ana/timeline`

### Seguidores de `bruno` (debería incluir a `ana`)
    
    `GET /seguimientos/bruno/seguidores`

### Seguidos por `ana`

    `GET /seguimientos/ana/seguidos`


---

## Observaciones

- Todos los datos generados se pueden repetir sin afectar el funcionamiento del sistema, ya que se usan identificadores constantes.
- Se recomienda ejecutar esta colección en un entorno limpio o luego de reiniciar la base de datos (por ejemplo, usando `./limpiar.sh`).


    