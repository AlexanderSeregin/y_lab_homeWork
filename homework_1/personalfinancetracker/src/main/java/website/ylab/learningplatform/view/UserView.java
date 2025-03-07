package website.ylab.learningplatform.view;

import website.ylab.learningplatform.datasource.NotificationDao;
import website.ylab.learningplatform.model.User;

public class UserView {
    public static void printNotification(String message) {
        ConsoleView.printLine("У Вас новое уведомление!");
        ConsoleView.printLine(message);
        ConsoleView.printLine("------------------------");
    }

    public static void printMenu(User user) {
        ConsoleView.printLine("Добро пожаловать, " + user.getName());
        ConsoleView.printLine("1. Управление профилем");
        ConsoleView.printLine("2. Управление финансами");
        ConsoleView.printLine("3. Управление бюджетом");
        ConsoleView.printLine("4. Управление целями");
        ConsoleView.printLine("5. Статистика и аналитика");
        ConsoleView.printLine("9. Сменить пользователя");
        ConsoleView.printLine("0. Выход");
    }

    public static void printSelfManagementMenu(User user) {
        ConsoleView.printLine("1. Изменить имя");
        ConsoleView.printLine("2. Изменить email");
        ConsoleView.printLine("3. Изменить пароль");
        ConsoleView.printLine("4. Удалить аккаунт");
        ConsoleView.printLine("0. Назад");
    }
    public static void printNotification(User user) {
        while (!NotificationDao.getInstance().get(user.getId()).isEmpty()) {
            NotificationDao.getInstance().get(user.getId()).ifPresent(notification ->
                    UserView.printNotification(notification.getMessage()));
        }
    }
}
