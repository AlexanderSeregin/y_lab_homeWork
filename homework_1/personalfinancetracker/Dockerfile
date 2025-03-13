# Этап сборки
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon


FROM openjdk:17-slim
WORKDIR /app

# Устанавливаем tcpserver (ucspi-tcp)
RUN apt-get update && apt-get install -y ucspi-tcp && rm -rf /var/lib/apt/lists/*


COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY src/main/resources/db/changelog/* /app/

EXPOSE 8186


CMD ["tcpserver", "-v", "0.0.0.0", "8186", "java", "-jar", "/app/app.jar"]