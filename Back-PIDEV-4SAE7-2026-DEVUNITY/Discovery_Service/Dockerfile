FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
RUN curl -o app.jar -L "http://192.168.22.130:8081/repository/Devunity-snapshots/tn/esprit/Discovery_Service/1.0-SNAPSHOT/Discovery_Service-1.0-20260502.140635-1.jar"
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]