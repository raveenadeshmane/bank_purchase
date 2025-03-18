# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /bankApp
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /bankApp
COPY --from=build /bankApp/target/*.jar purchase_approval.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "purchase_approval.jar"]

