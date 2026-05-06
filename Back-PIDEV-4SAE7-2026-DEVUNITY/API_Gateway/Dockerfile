FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY Back-PIDEV-4SAE7-2026-DEVUNITY/API_Gateway/target/API_Gateway-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]