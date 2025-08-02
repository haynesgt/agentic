FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /home/app

COPY pom.xml ./
COPY agent/pom.xml ./agent/pom.xml
COPY server/pom.xml ./server/pom.xml
COPY worker/pom.xml ./worker/pom.xml
COPY common/pom.xml ./common/pom.xml

RUN mvn dependency:go-offline -B

COPY . .

RUN mvn clean install

RUN find  -name '*.jar'

FROM openjdk:21-jdk-slim

ARG POSTGRES_USER
ARG POSTGRESS_PASS
ARG POSTGRESS_SCHEMA=dev

WORKDIR /home/app

COPY --from=build /home/app/server/target/server-1.0-SNAPSHOT.jar app.jar

RUN echo flyway.user=${POSTGRES_USER} \
    flyway.password=${POSTGRES_PASS} \
    flyway.schemas=${POSTGRES_SCHEMA} > flyway.conf

CMD ["java","-jar","app.jar"]
