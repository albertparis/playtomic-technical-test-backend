# Use Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK image to run the application
FROM openjdk:17-jdk-alpine
EXPOSE 8090
ARG JAR_FILE=target/exercise-wallet-0.0.2.jar
COPY --from=build /app/target/exercise-wallet-0.0.2.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]