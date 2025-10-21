# Usamos la imagen oficial de Java 17
FROM eclipse-temurin:17-jdk

# Establecer directorio de trabajo
WORKDIR /app

# Copiar los archivos de Maven y el proyecto
COPY . .

# Compilar el proyecto
RUN ./mvnw -B -DskipTests clean package

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/fisio-0.0.1-SNAPSHOT.war"]
