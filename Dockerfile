FROM openjdk:11-jdk-slim
ARG target=target/*.jar
COPY ${target} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
