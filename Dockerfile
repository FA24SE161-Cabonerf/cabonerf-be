# Stage 1: Build
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy only the pom.xml and any settings files
COPY pom.xml .

# Download dependencies without building the application (caches dependencies)
RUN mvn dependency:resolve -DskipTests

# Copy the source files last to leverage caching when dependencies haven’t changed
COPY src ./src

# Clean and package the application
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-oracle

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Use optimal entropy source for faster startups
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]

# Expose default Spring Boot port
EXPOSE 8080

# Optional: Health check
# HEALTHCHECK --interval=30s --timeout=30s --start-period=30s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1
