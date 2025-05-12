#!/bin/bash

set -e

echo "Levantando servicios necesarios..."
docker compose down
docker compose up -d postgres kafka zookeeper

echo "Esperando hasta 20 seg que PostgreSQL y Kafka estén listos..."
for i in {1..20}; do
  if nc -z localhost 5432 && nc -z localhost 9092; then
    echo "PostgreSQL y Kafka listos"
    break
  fi
  echo "Esperando..."
  sleep 1
done

echo "Compilar y empaquetar"
./gradlew clean bootJar

echo "Empaqueta app"
docker build -t tuiter-app .

echo "Levantar app"
docker run --rm -p 8080:8080 --env-file .env tuiter-app
