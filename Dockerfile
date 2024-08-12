FROM openjdk:17-jdk-alpine
EXPOSE 8090
ARG JAR_FILE=target/exercise-wallet-0.0.2.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]