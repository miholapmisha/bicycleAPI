# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle files and scripts
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Copy the source code
COPY src/ src/

# Build the application
RUN ./gradlew build

# Expose the port that the application will run on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "build/libs/bicycleAPI-0.0.1-SNAPSHOT-plain.jar"]