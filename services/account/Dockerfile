FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY . .
RUN ./mvnw package -DskipTests -q
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/target/account-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
