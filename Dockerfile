# Use official maven image for build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build


WORKDIR /home/app

COPY pom.xml ./
COPY agent/pom.xml ./agent/pom.xml
COPY server/pom.xml ./server/pom.xml
COPY worker/pom.xml ./worker/pom.xml

# build all dependencies for offline use
RUN mvn dependency:go-offline -B

# copy the project files
COPY ./ /home/app/

# build the project
RUN mvn clean install


RUN find -name '*.jar'

# stage to run the spring boot application
FROM openjdk:21-jdk-slim

COPY --from=build /home/app/server/target/*.jar app.jar

# the default command to run when starting a container with this image
## CMD ["java","-jar","app.jar"]

