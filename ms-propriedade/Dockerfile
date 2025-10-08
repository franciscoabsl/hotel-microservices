FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY ms-apigateway/pom.xml ./ms-apigateway/
COPY ms-authuser/pom.xml ./ms-authuser/
COPY ms-notificacao/pom.xml ./ms-notificacao/
COPY ms-reserva/pom.xml ./ms-reserva/

COPY ms-propriedade/pom.xml ./ms-propriedade/
COPY ms-propriedade/src ./ms-propriedade/src

RUN mvn dependency:go-offline -B -DskipTests

RUN mvn clean install -pl ms-propriedade -am -Dfile.encoding=UTF-8 -B -V -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/ms-propriedade/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]