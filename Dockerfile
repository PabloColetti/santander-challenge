# Dockerfile - Multi-stage para construir los microservicios
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar el pom.xml raíz y todos los pom.xml de los microservicios
COPY pom.xml .
COPY ms-config/pom.xml ./ms-config/
COPY ms-eureka/pom.xml ./ms-eureka/
COPY api-consumer/pom.xml ./api-consumer/
COPY ms-banks/pom.xml ./ms-banks/
COPY ms-accounts/pom.xml ./ms-accounts/

# Descargar dependencias (caché de Maven) - solo se ejecuta si cambian los pom.xml de los microservicios
RUN mvn dependency:go-offline -B

# Copiar el código fuente de los microservicios
COPY ms-config ./ms-config
COPY ms-eureka ./ms-eureka
COPY api-consumer ./api-consumer
COPY ms-banks ./ms-banks
COPY ms-accounts ./ms-accounts

# Construir todos los proyectos
RUN mvn clean package -DskipTests

# Imagen final con Java runtime
FROM eclipse-temurin:17-jre-alpine

# Instalar wget y netcat para healthchecks
RUN apk add --no-cache wget netcat-openbsd

WORKDIR /app

# Copiar el JAR construido (se especifica en el archivo docker-compose.yml)
ARG JAR_FILE
COPY --from=build /app/${JAR_FILE} app.jar

# Puerto expuesto (se especifica en el archivo docker-compose.yml)
ARG EXPOSE_PORT=8080
EXPOSE ${EXPOSE_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]

