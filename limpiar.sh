#!/bin/bash
set -e

echo "Deteniendo y eliminando contenedores del proyecto tuiter..."
docker compose -f docker-compose.yml -p tuiter down

echo "Eliminando volumen de datos (pgdata)..."
docker volume rm tuiter_pgdata || echo "Volumen no encontrado, quizás borrado"

echo "Eliminando imagen de la app (tuiter-app)..."
docker rmi tuiter-app || echo "Imagen no encontrada"

echo "Limpieza completada."
