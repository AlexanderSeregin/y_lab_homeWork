# Этап сборки
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Финальный образ
FROM openjdk:17-slim
WORKDIR /app

# Устанавливаем tcpserver (ucspi-tcp)
RUN apt-get update && apt-get install -y ucspi-tcp && rm -rf /var/lib/apt/lists/*

# Копируем готовый jar-файл
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Открываем порт 8186 (по нему будем подключаться)
EXPOSE 8186

# Запускаем tcpserver: при каждом новом подключении будет
# порождаться команда java -jar /app/app.jar
CMD ["tcpserver", "-v", "0.0.0.0", "8186", "java", "-jar", "/app/app.jar"]