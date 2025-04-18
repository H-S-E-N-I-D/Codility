# Stage 1: Build with Maven
FROM maven:3.8.5-openjdk-17 AS builder

# Set working directory
WORKDIR /app

# Copy only the POM file first (for better layer caching)
COPY pom.xml .

# Download all dependencies (this layer will be cached unless POM changes)
RUN mvn dependency:go-offline -B

# Copy source files
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Runtime with JRE
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]