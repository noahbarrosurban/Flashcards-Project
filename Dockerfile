FROM gradle:8.5-jdk17 AS builder

COPY . /app

WORKDIR /app

RUN gradle build -x test

FROM eclipse-temurin:17-jdk

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
