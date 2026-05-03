FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/Social_Interaction_Service-1.0-SNAPSHOT.jar Social_Interaction_Service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "Social_Interaction_Service.jar"]
