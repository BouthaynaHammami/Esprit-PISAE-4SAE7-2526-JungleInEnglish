FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY Back-PIDEV-4SAE7-2026-DEVUNITY/Discovery_Service/target/Discovery_Service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]