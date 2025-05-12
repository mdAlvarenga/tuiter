#!/bin/bash
set -e

echo "Bajando Kafka, Zookeeper, Postgres y Redis..."
docker compose -f docker-compose.yml -p tuiter down

echo "Eliminando volumen de Kafka (logs)..."
docker volume rm tuiter_kafka-data || echo "Volumen de Kafka no encontrado"

echo "Eliminando volumen de Postgres (pgdata)..."
docker volume rm tuiter_pgdata || echo "Volumen de Postgres no encontrado"

echo "Eliminando volumen de Redis..."
docker volume rm tuiter_redis-data || echo "Volumen de Redis no encontrado"

echo "Eliminando imagen de la app (tuiter-app)..."
docker rmi tuiter-app || echo "Imagen no encontrada"

echo "Limpieza completada."y es