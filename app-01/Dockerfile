FROM openjdk:17-oracle AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17-oracle
WORKDIR app-01
COPY --from=build target/*.jar weatherapp.jar
ENTRYPOINT ["java","-jar","weatherapp.jar"]