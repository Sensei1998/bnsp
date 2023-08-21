FROM maven:3.5.4-jdk-11 AS build
WORKDIR /app
COPY target/api-0.0.1-SNAPSHOT.jar .
EXPOSE 8082
ENTRYPOINT ["java","-jar","api-0.0.1-SNAPSHOT.jar"]