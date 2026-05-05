FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl

# Download JAR from Nexus
RUN curl -f -L -o app.jar "http://192.168.232.133:8081/repository/devunity-repo/tn/esprit/Community_Engagement_Service/1.0-SNAPSHOT/Community_Engagement_Service-1.0-SNAPSHOT.jar"

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]