package website.ylab.learningplatform.view;

import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.NotificationService;

import java.math.BigDecimal;
import java.util.Map;

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
        Notification notification = null;
        while ((notification = NotificationService.getNotification(user)) != null) {
            printNotification(notification.getMessage());
        }

    }

    public static void printTransactionMenu(User user) {
        ConsoleView.printLine("1. Создать транзакцию");
        ConsoleView.printLine("2. Просмотреть транзакции");
        ConsoleView.printLine("3. Изменить транзакцию");
        ConsoleView.printLine("4. Удалить транзакцию");
        ConsoleView.printLine("0. Назад");

    }

    public static void askTransactionIncome() {
        ConsoleView.printLine("Это приход? (Да/Нет)");
    }

    public static void askDescription() {
        ConsoleView.printLine("Введите описание транзакции:");
    }

    public static void askAmount() {
        ConsoleView.printLine("Введите сумму транзакции:");
    }

    public static void askCategory() {
        ConsoleView.printLine("Выберите категорию транзакции из списка:");
        int i = 1;
        for (Category category : Category.values()) {
            ConsoleView.printLine(i + ". " + category.name());
            i++;
        }
    }

    public static void printSuccess() {
        ConsoleView.printLine("Данные успешно обновлены!");
    }

    public static void askTransactionId() {
        ConsoleView.printLine("Введите id транзакции:");
    }

    public static void askWhatToChange() {
        ConsoleView.printLine("Что вы хотите изменить?");
        ConsoleView.printLine("1. Описание");
        ConsoleView.printLine("2. Сумма");
        ConsoleView.printLine("3. Категория");
    }

    public static void askBudget() {
        ConsoleView.printLine("Введите сумму бюджета:");
    }

    public static void printBudgetMenu(User user) {
        ConsoleView.printLine("1. Просмотреть текущий бюджет на месяц");
        ConsoleView.printLine("2. Изменить текущий бюджет на месяц");
        ConsoleView.printLine("0. Назад");
    }

    public static void printCurrentGoalStats(Goal goal, BigDecimal savings) {
        ConsoleView.printLine("Ваша текущая цель накопления: " + goal.getAmount());
        ConsoleView.printLine("Вы накопили: " + savings);
    }

    public static void askGoal() {
        ConsoleView.printLine("Введите сумму цели накопления:");
    }

    public static void printGoalMenu(User user) {
        ConsoleView.printLine("1. Просмотреть текущую цель накопления");
        ConsoleView.printLine("2. Изменить текущую цель накопления");
        ConsoleView.printLine("0. Назад");
    }

    public static void printUserInfo(User user) {
        ConsoleView.printLine("Имя: " + user.getName());
        ConsoleView.printLine("Email: " + user.getEmail());
        ConsoleView.printLine("Баланс: " + user.getBalance());
    }

    public static void printCurrentBudgetStats(BigDecimal budget, BigDecimal sumOfUserSpendingsForCurrentMonth) {
        ConsoleView.printLine("Ваш текущий бюджет на месяц: " + budget);
        ConsoleView.printLine("Вы потратили: " + sumOfUserSpendingsForCurrentMonth);
    }

    public static void printErrorNoTransactions() {
        ConsoleView.printLine("Нет транзакций");
    }

    public static void printCurrentBudget(BigDecimal budget) {
        ConsoleView.printLine("Ваш текущий бюджет на месяц: " + budget);
    }

    public static void printSpendingsByCategory(Map<Category, BigDecimal> sumOfUserSpendingsByCategoryForCurrentMonth) {
        if (sumOfUserSpendingsByCategoryForCurrentMonth == null) {
            ConsoleView.printLine("Нет транзакций");
            return;
        }
        for (Map.Entry<Category, BigDecimal> entry : sumOfUserSpendingsByCategoryForCurrentMonth.entrySet()) {
            ConsoleView.printLine(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void printUserStats(BigDecimal sumOfUserSpendingsForCurrentMonth, BigDecimal sumOfUserIncomesForCurrentMonth) {
        ConsoleView.printLine("Вы потратили: " + sumOfUserSpendingsForCurrentMonth);
        ConsoleView.printLine("Вы получили: " + sumOfUserIncomesForCurrentMonth);
    }

    public static void printUserBalance(User user) {
        ConsoleView.printLine("Ваш баланс: " + user.getBalance());
    }

    public static void askStatsDateFromTo() {
        ConsoleView.printLine("Введите дату начала в формате ДД.ММ.ГГГГ:");
        ConsoleView.printLine("Введите дату окончания в формате ДД.ММ.ГГГГ:");
    }

    public static void askStats() {
        ConsoleView.printLine("1. Просмотреть баланс");
        ConsoleView.printLine("2. Расход и доход за период");
        ConsoleView.printLine("3. Расходы по категориям за текущий месяц");
    }
}

