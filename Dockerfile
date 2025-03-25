# Start with a Maven image to build the application
FROM maven:3.8.7-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download all dependencies
RUN mvn dependency:go-offline -B

# Copy the project source
COPY src ./src

# Package the application
RUN mvn package -DskipTests

# Use a lightweight JRE image for the runtime environment
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
