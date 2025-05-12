#!/bin/bash
set -e

echo "Bajando servicios previos (si existen)..."
docker compose down

echo "Levantando servicios necesarios (Postgres, Kafka, Zookeeper, Redis)..."
docker compose up -d postgres kafka zookeeper redis

echo "Esperando hasta 20 seg que PostgreSQL, Kafka y Redis estén listos..."
for i in {1..20}; do
  if nc -z localhost 5432 && nc -z localhost 9092 && nc -z localhost 6379; then
    echo "PostgreSQL, Kafka y Redis listos"
    break
  fi
  echo "Esperando..."
  sleep 1
done

echo "Verificando red Docker..."
if ! docker network ls | grep -q "tuiter-red"; then
  echo "ERROR: la red 'tuiter-red' no fue creada. Abortando."
  docker network ls
  exit 1
fi

echo "Compilar y empaquetar"
./gradlew clean bootJar

echo "Empaquetando imagen Docker..."
docker build -t tuiter-app .

echo "Ejecutando app conectada a red 'tuiter-red'"
docker run --rm --network tuiter-red -p 8080:8080 --env-file .env tuiter-app
