FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY ../Common/pom.xml Common/pom.xml
COPY ../../OrderTrack/pom.xml Order/pom.xml
COPY ../Order/pom.xml Order/pom.xml

RUN mvn dependency:go-offliine -B

COPY ../Common/src Common/src
COPY ../Order/src Order/src

RUN mvn clean install -DskipTests
RUN mvn package -pl Order -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/Order/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

