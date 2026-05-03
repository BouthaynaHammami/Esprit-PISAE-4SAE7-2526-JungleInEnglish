FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
RUN curl -o app.jar -L "http://192.168.22.130:8081/repository/Devunity-snapshots/tn/esprit/Academic_Management_Service/1.0-SNAPSHOT/Academic_Management_Service-1.0-20260502.140637-1.jar"
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]