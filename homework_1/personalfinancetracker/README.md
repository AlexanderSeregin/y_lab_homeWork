# Основной README файл.

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
