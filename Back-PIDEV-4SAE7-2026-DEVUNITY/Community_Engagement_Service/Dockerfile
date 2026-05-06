FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY Back-PIDEV-4SAE7-2026-DEVUNITY/Community_Engagement_Service/target/Community_Engagement_Service-1.0-SNAPSHOT.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]