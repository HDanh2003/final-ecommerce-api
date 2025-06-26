FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/EcommerceAPI-0.0.1-SNAPSHOT.jar app.jar
RUN ls -l /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]