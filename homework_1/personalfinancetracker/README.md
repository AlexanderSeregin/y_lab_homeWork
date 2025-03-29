# Основной README файл.

1. При успешной авторизации пользователю в заголовке возвращается x-auth-token и дальнейшая работа с API осуществляется только если в заголовке передаётся корректный токен.
2. Swagger доступен по адресу http://localhost:8080/swagger-ui.html или http://test2.ev1l.ru:8080/swagger-ui.html
3. Запуск локально:
   cd homework_1/docker-elk-main
   docker compose up setup
   docker compose up -d
   cd ../personalfinancetracker/
   docker compose up