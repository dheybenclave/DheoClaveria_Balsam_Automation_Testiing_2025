# Multi-stage Dockerfile for Java 21 + Gradle wrapper
# Stage 1: build with full JDK
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src

# Copy project
COPY . /src

# Ensure the Gradle wrapper is executable and build the project
RUN chmod +x ./gradlew
# Build the project, skip tests to keep image build fast (CI already runs tests earlier)
RUN ./gradlew clean build --no-daemon -x test

# Stage 2: runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the application JAR from the build stage. Adjust pattern if your build produces a different artifact.
COPY --from=build /src/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
