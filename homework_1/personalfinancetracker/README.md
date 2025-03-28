# Основной README файл.

## Spring Boot Migration

This project has been migrated to Spring Boot to improve maintainability, scalability, and development efficiency. The migration includes:

1. Implemented RESTful controllers for all operations (Authentication, User, Transaction, Budget, Notification)
2. Integrated Spring Data JDBC for database access
3. Added Swagger/OpenAPI documentation for API endpoints
4. Implemented proper dependency injection through constructors
5. Centralized configuration using application.yml

## Original Features

1. Я реализовал сессионную авторизацию пользователя.
2. Хранить логи аудита в продакшн базе мне показалось плохой идеей, поэтому прикрутил ELK.
3. Две коллекции постмана лежат в корне проекта.
   RemotePersonalFinancialTracker - для проверки размещённого у меня проекта, второй - для локального запуска
4. Запуск локально: (на Макос docker-compose через дефис)
* cd homework_1/docker-elk-main
* docker compose up setup
* docker compose up -d
* cd ../personalfinancetracker/
* docker compose up
5. Само приложение доступно на порту 8080 (test2.ev1l.ru или localhost)
   Логи доступны на http://localhost:5601/app/logs или http://test2.ev1l.ru:5601/app/logs логин elastic пароль veryHardPass
6. api управления пользовалятелями и несколько старых замечаний в процессе...

## API Documentation

The API documentation is available at http://localhost:8080/swagger-ui.html after starting the application.

## Technologies Used

- Spring Boot 2.7.x
- Spring Data JDBC
- PostgreSQL
- Liquibase for database migrations
- Swagger/OpenAPI for API documentation
- Docker for containerization
- ELK stack for logging