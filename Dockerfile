# Imagen base con Java 17 y Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos y compilar
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa final con solo el artefacto
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/fisio-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
