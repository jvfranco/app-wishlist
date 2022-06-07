FROM openjdk:17-alpine
MAINTAINER Joao Victor Franco Rosa
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080