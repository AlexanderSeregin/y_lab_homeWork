# Основной README файл.

Swagger доступен по адресу http://localhost:8080/swagger-ui.html или http://test2.ev1l.ru:8080/swagger-ui.html 
Openapi доступен по адресу http://localhost:8080/api-docs или http://test2.ev1l.ru:8080/api-docs
Elasticsearch доступен по адресу http://localhost:5601/ или http://test2.ev1l.ru:5601/ (логин elastic пароль veryHardPass)

Запуск локально: 
cd homework_1/docker-elk-main 
docker compose up setup 
docker compose up -d 
cd ../personalfinancetracker/ 
docker compose up
