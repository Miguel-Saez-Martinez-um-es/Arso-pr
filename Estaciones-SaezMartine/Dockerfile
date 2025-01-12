FROM maven:3.8.5-jdk-8 AS builder

WORKDIR /app/
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM openjdk:8
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]