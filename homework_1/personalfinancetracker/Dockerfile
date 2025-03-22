# Этап сборки
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon


FROM openjdk:17-slim
WORKDIR /app

# Устанавливаем tcpserver (ucspi-tcp)


COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY src/main/resources/db/changelog/* /app/

EXPOSE 8080


CMD ["java", "-jar", "/app/app.jar"]