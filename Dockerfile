# Use an official OpenJDK runtime as a base image
FROM adoptopenjdk:17-jre-hotspot

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
CMD ["java", "-jar", "build/libs/your-application-name-0.0.1-SNAPSHOT.jar"]
