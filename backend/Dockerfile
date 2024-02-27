
FROM openjdk:17-jdk-alpine

WORKDIR /var/backend

COPY /target/*.jar /app.jar

EXPOSE 5556

ENTRYPOINT ["java", "-jar", "/app.jar"]
