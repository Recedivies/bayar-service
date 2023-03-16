FROM openjdk:19-alpine
ARG JAR_FILE=build/libs/bayar-service-0.0.1-SNAPSHOT.jar

WORKDIR /app
COPY .env wait-for.sh ./
COPY ${JAR_FILE} app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
