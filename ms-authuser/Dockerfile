FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY ms-authuser/pom.xml ./ms-authuser/pom.xml

RUN mvn -B -ntp -f ms-authuser/pom.xml dependency:go-offline

COPY . .

RUN mvn -B -ntp -f ms-authuser/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/ms-authuser/target/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50 -Dfile.encoding=UTF-8"
CMD ["java", "-jar", "app.jar"]