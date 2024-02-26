
FROM openjdk:17-jdk-alpine

WORKDIR /var/backend

COPY target/*.jar /app.jar

EXPOSE 5556

CMD ["java", "-jar", "/app.jar"]
