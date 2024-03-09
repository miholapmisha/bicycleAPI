FROM gradle:8.5.0-jdk17 AS build
COPY . .
RUN gradle build --no-daemon

FROM openjdk:17.0.1-jdk-slim
COPY --from=build build/libs/bicycleAPI-0.0.1-SNAPSHOT-plain.jar bicycleAPI-0.0.1-SNAPSHOT-plain.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bicycleAPI-0.0.1-SNAPSHOT-plain.jar"]


