FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY ms-apigateway/pom.xml ./ms-apigateway/
COPY ms-notificacao/pom.xml ./ms-notificacao/
COPY ms-propriedade/pom.xml ./ms-propriedade/
COPY ms-reserva/pom.xml ./ms-reserva/

COPY ms-authuser/pom.xml ./ms-authuser/
COPY ms-authuser/src ./ms-authuser/src

RUN mvn dependency:go-offline -B -DskipTests

RUN mvn clean install -pl ms-authuser -am -Dfile.encoding=UTF-8 -B -V -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/ms-authuser/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]