FROM maven:3.8.5-jdk-8 AS builder
WORKDIR /app/
COPY pom.xml .
COPY src ./src
RUN mvn package 

FROM maven:3.8.5-jdk-8
WORKDIR /app
COPY pom.xml .
COPY src ./src
CMD ["mvn", "jetty:run"]

