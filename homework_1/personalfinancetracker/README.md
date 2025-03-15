# Основной README файл.

1. Уже собранный и работающий проект доступен через старый добрый TCP. Это консольное приложение. Просто ввод и вывод перенаправляются.
   Просто запускаем в терминале
    nc test1.ev1l.ru 8186
    или
    telnet test1.ev1l.ru 8186 (стартует около 20 секунд)
2. Сборка и запуск локально
   docker-compose up -d (или на линуксе docker compose up -d)
   далее можно или подключиться
   nc localhost  8186
   или запустить приложение внутри контейнера
   docker exec -it personalfinancetracker-gradle-console-app-1 sh -c "java -jar /app/app.jar"
3. Учётные записи для тестирования
    email: user@user.com password: user
    email: admin@admin.com password: admin
4. База данных. Индексы добавил только на столбец с email таблицы users и столбец transaction_date
таблицы transactions (возможно тут составной индекс работал бы лучше, но врятле это принципиально).
Подключение к базе: postgresql://localhost:5234/finance_tracker (наружу порт не стандартный для отсутствия конфликтов. Контейнеры подключаются на порт 5432).
login: finance_user password: finance_password
4. Самокритика.
    В Testcontainers использовал статический порт. Уже потом понял, что это неправильно и почему...
    <Здесь было ещё много самокритики :)>
    