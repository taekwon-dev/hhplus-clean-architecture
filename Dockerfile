FROM amazoncorretto:17-alpine-jdk

ARG JAR_FILE=./build/libs/architecture-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} hhplus-app.jar

ENTRYPOINT ["java", "-jar", "/hhplus-app.jar", "-Dspring.config.location=/application.yml"]
