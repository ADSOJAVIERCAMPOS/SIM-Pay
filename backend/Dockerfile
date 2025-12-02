# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Imagen de producción
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no privilegiado
RUN addgroup -g 1001 -S simpay && \
    adduser -S simpay -u 1001

# Copiar el JAR construido
COPY --from=builder /app/target/*.jar app.jar

# Cambiar al usuario no privilegiado
USER simpay

# Configurar variables de entorno
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Exponer el puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]