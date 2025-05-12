# Datos generados por la colección de Postman

La colección `Challenge-Tuiter.postman_collection.json` automatiza la creación de relaciones de seguimiento y la publicación de tuits desde un archivo CSV, con el objetivo de facilitar pruebas del sistema.

---

## Relaciones de seguimiento

Durante la ejecución de la colección:

- `ana` comienza a seguir a:
    - `bruno`
    - `carla`
    - `dario`
    - `elena`

---

## Publicación de tuits

El archivo `tuits.csv` contiene 10 registros por cada uno de los siguientes usuarios:

- `bruno`
- `carla`
- `dario`
- `elena`

Cada línea genera un tuit con contenido predefinido para pruebas, del tipo:

    Contenido aleatorio para {{autorId}}

---

## Consultas ejecutadas por la colección

La colección realiza los siguientes requests de validación:

- Timeline de `ana`: `GET /usuarios/ana/timeline

- Seguidores de `bruno` (debería incluir a `ana`): `GET /seguimientos/bruno/seguidores`

- Seguidos por `ana`: `GET /seguimientos/ana/seguidos`

---

## Consideraciones

- Para reiniciar el estado de la base de datos, ejecutar `./limpiar.sh`.