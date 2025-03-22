# Этап сборки
FROM gradle:jdk17-corretto AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon


FROM openjdk:17-slim
WORKDIR /app


COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY src/main/resources/db/changelog/* /app/

EXPOSE 8080


CMD ["java", "-jar", "/app/app.jar"]