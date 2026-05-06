FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY Back-PIDEV-4SAE7-2026-DEVUNITY/Academic_Management_Service/target/Academic_Management_Service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]