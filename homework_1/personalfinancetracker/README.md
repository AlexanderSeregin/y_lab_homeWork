# Основной README файл.

1. Требование "Внедрение зависимостей ТОЛЬКО через конструктор" на 100% реализовать не удалось.
Liquibase запускать нужно до старта приложения, когда бинов ещё не существует. Так что экземпляр 1 класса создаётся вручную.
2. При успешной авторизации пользователю в заголовке возвращается x-auth-token и дальнейшая работа с API осуществляется только если в заголовке передаётся корректный токен.
3. Swagger доступен по адресу http://localhost:8080/swagger-ui.html или http://test2.ev1l.ru:8080/swagger-ui.html
4. Запуск локально:
   cd homework_1/docker-elk-main
   docker compose up setup
   docker compose up -d
   cd ../personalfinancetracker/
   docker compose up