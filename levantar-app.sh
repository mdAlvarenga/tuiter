#!/bin/bash

set -e

echo "Levanta db con Docker"
docker compose down
docker compose up -d postgres

echo "Esperando hasta 20 seg que PostgreSQL esté listo..."
for i in {1..20}; do
  if nc -z localhost 5432; then
    echo "PostgreSQL listo"
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
