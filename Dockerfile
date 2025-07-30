FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /home/app

COPY pom.xml ./
COPY agent/pom.xml ./agent/pom.xml
COPY server/pom.xml ./server/pom.xml
COPY worker/pom.xml ./worker/pom.xml
COPY common/pom.xml ./common/pom.xml

RUN mvn dependency:go-offline -B

COPY . /home/app/

RUN mvn clean install

RUN find  -name '*.jar'

FROM openjdk:21-jdk-slim

COPY --from=build /home/app/server/target/*.jar app.jar

CMD ["java","-jar","app.jar"]
