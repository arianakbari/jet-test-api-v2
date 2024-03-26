# Use an OpenJDK base image
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven configuration files
COPY mvnw .
COPY .mvn .mvn

# Copy the project files and build the application
COPY pom.xml .
COPY src src
RUN ./mvnw clean package -DskipTests

# Use a lightweight base image for the runtime environment
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the packaged JAR file from the builder stage to the runtime image
COPY --from=builder /app/target/*.jar app.jar

# Expose the port on which the Spring Boot application will run
EXPOSE ${PORT}

# Define the command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]