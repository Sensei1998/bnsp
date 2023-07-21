FROM openjdk:11
LABEL authors="silver"
VOLUME /tmp
EXPOSE 8282
ARG JAR_FILE=target/api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]