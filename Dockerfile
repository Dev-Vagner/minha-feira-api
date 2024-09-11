FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -X -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build ./target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]