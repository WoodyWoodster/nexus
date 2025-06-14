FROM openjdk:21-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]