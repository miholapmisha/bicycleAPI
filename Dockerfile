FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/
COPY src/ src/
RUN ./gradlew build
EXPOSE 8080
CMD ["java", "-jar", "build/libs/demo.jar"]