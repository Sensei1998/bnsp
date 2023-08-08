FROM openjdk:11
VOLUME /tmp
COPY target/api-0.0.1-SNAPSHOT.jar .
EXPOSE 8082
ENTRYPOINT ["java","-jar","api-0.0.1-SNAPSHOT.jar"]