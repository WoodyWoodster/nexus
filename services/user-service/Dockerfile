FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=build/libs/*.jar
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
