# Build stage
FROM maven:3.9-sapmachine-17 AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn -f pom.xml clean package

# Run stage
FROM maven:3.9-sapmachine-17
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
