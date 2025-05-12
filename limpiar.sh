#!/bin/bash
set -e

echo "Bajando Kafka y Zookeeper..."
docker compose -f docker-compose.yml -p tuiter down

echo "Eliminando volumen de Kafka (logs)..."
docker volume rm tuiter_kafka-data || echo "Volumen de Kafka no encontrado"

echo "Eliminando volumen de datos (pgdata)..."
docker volume rm tuiter_pgdata || echo "Volumen de Postgres no encontrado"

echo "Eliminando imagen de la app (tuiter-app)..."
docker rmi tuiter-app || echo "Imagen no encontrada"

echo "Limpieza completada."