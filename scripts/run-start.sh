#!/usr/bin/env bash

set -euo pipefail


GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' 

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT"


declare -a JAVA_PIDS=()


cleanup() {
  echo ""
  echo -e "${YELLOW}==> Deteniendo servicios...${NC}"
  
  
  for pid in "${JAVA_PIDS[@]}"; do
    if [ -n "$pid" ] && kill -0 "$pid" 2>/dev/null; then
      kill "$pid" 2>/dev/null || true
    fi
  done
  
  
  sleep 2
  
  
  for pid in "${JAVA_PIDS[@]}"; do
    if [ -n "$pid" ] && kill -0 "$pid" 2>/dev/null; then
      kill -9 "$pid" 2>/dev/null || true
    fi
  done
  
  
  docker-compose stop postgres-banks postgres-accounts > /dev/null 2>&1 || true
  
  echo -e "${GREEN}✓ Todos los servicios detenidos${NC}"
  exit 0
}


trap cleanup SIGINT SIGTERM


BUILD=false
if [[ "$*" == *"--build"* ]] || [[ "$*" == *"-b"* ]]; then
  BUILD=true
fi


if [ "$BUILD" = true ]; then
  echo -e "${BLUE}========================================${NC}"
  echo -e "${BLUE}Compilando todos los módulos...${NC}"
  echo -e "${BLUE}========================================${NC}"

  echo -e "${YELLOW}==> Compilando ms-config...${NC}"
  cd ms-config
  mvn clean install -DskipTests -q
  cd ..

  echo -e "${YELLOW}==> Compilando ms-eureka...${NC}"
  cd ms-eureka
  mvn clean install -DskipTests -q
  cd ..

  echo -e "${YELLOW}==> Compilando api-consumer...${NC}"
  cd api-consumer
  mvn clean install -DskipTests -q
  cd ..

  echo -e "${YELLOW}==> Compilando ms-banks...${NC}"
  cd ms-banks
  mvn clean install -DskipTests -q
  cd ..

  echo -e "${YELLOW}==> Compilando ms-accounts...${NC}"
  cd ms-accounts
  mvn clean install -DskipTests -q
  cd ..

  echo -e "${GREEN}✓ Compilación completada${NC}"
  echo ""
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Levantando servicios...${NC}"
echo -e "${BLUE}========================================${NC}"


wait_for_service() {
  local service_name=$1
  local url=$2
  local max_attempts=30
  local attempt=1

  while [ $attempt -le $max_attempts ]; do
    if curl -s "$url" 2>/dev/null | grep -q '"status":"UP"'; then
      echo -e "${GREEN}✓ ${service_name} está UP${NC}"
      return 0
    fi
    sleep 2
    attempt=$((attempt + 1))
  done
  echo -e "${YELLOW}⚠ ${service_name} no respondió después de $((max_attempts * 2)) segundos${NC}"
  return 1
}


wait_for_db() {
  local db_name=$1
  local max_attempts=15
  local attempt=1

  while [ $attempt -le $max_attempts ]; do
    if docker ps --filter "name=${db_name}" --format "{{.Status}}" | grep -q "healthy"; then
      echo -e "${GREEN}✓ ${db_name} está healthy${NC}"
      return 0
    fi
    sleep 2
    attempt=$((attempt + 1))
  done
  echo -e "${YELLOW}⚠ ${db_name} no está healthy después de $((max_attempts * 2)) segundos${NC}"
  return 1
}


echo -e "${GREEN}==> Levantando ms-config...${NC}"
cd ms-config
mvn spring-boot:run &
MS_CONFIG_PID=$!
JAVA_PIDS+=($MS_CONFIG_PID)
cd ..
wait_for_service "ms-config" "http://localhost:8888/actuator/health" || wait_for_service "ms-config" "http://localhost:8888/"


echo -e "${GREEN}==> Levantando ms-eureka...${NC}"
cd ms-eureka
mvn spring-boot:run -Dspring-boot.run.profiles=local &
MS_EUREKA_PID=$!
JAVA_PIDS+=($MS_EUREKA_PID)
cd ..
wait_for_service "ms-eureka" "http://localhost:8761/actuator/health"


echo -e "${GREEN}==> Levantando postgres-banks...${NC}"
docker-compose up -d postgres-banks > /dev/null 2>&1
wait_for_db "postgres-banks"


echo -e "${GREEN}==> Levantando ms-banks...${NC}"
cd ms-banks
mvn spring-boot:run -Dspring-boot.run.profiles=local &
MS_BANKS_PID=$!
JAVA_PIDS+=($MS_BANKS_PID)
cd ..
wait_for_service "ms-banks" "http://localhost:8090/actuator/health"


echo -e "${GREEN}==> Levantando postgres-accounts...${NC}"
docker-compose up -d postgres-accounts > /dev/null 2>&1
wait_for_db "postgres-accounts"


echo -e "${GREEN}==> Levantando ms-accounts...${NC}"
cd ms-accounts
mvn spring-boot:run -Dspring-boot.run.profiles=local &
MS_ACCOUNTS_PID=$!
JAVA_PIDS+=($MS_ACCOUNTS_PID)
cd ..
wait_for_service "ms-accounts" "http://localhost:9090/actuator/health"


echo -e "${GREEN}==> Levantando api-consumer...${NC}"
cd api-consumer
mvn spring-boot:run -Dspring-boot.run.profiles=local &
API_CONSUMER_PID=$!
JAVA_PIDS+=($API_CONSUMER_PID)
cd ..
wait_for_service "api-consumer" "http://localhost:8080/actuator/health"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ Todos los servicios están levantados!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}Presiona Ctrl+C para detener todos los servicios${NC}"
echo ""



wait

