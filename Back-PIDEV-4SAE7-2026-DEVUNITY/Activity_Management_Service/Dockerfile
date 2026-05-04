FROM openjdk:17-jdk-slim

WORKDIR /app

COPY Activity_Management_Service/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
