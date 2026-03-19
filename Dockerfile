# FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp
# COPY target/*.jar BACKEND.jar
# ENTRYPOINT ["java","-jar","/BACKEND.jar"]
# EXPOSE 9595
# FROM maven:3.8.5-openjdk-17 AS build
# COPY . .
# RUN mvn clean package -DskipTests

# FROM openjdk:17.0.1-jdk-slim
# COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","demo.jar"]
# FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp
# # COPY target/*.jar App.jar
# COPY --from=build /target/App-0.0.1-SNAPSHOT.jar App.jar
# ENTRYPOINT ["java","-jar","/App.jar"]
# EXPOSE 9595
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 9595