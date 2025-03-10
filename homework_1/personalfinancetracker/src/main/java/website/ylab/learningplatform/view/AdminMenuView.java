package website.ylab.learningplatform.view;

import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.util.List;
import java.util.Optional;

public class AdminMenuView {
    public static void printAdminMenu() {
        ConsoleView.printLine("1. Просмотреть список пользователей");
        ConsoleView.printLine("2. Просмотреть транзакции пользователя");
        ConsoleView.printLine("3. Заблокировать пользователя");
        ConsoleView.printLine("9. Сменить пользователя");
        ConsoleView.printLine("0. Выход");
    }

    public static void printUsers(Iterable<User> all) {
        for (User user : all) {
            ConsoleView.printLine(user.getId() + " " + user.getName() + " " + user.getEmail());
        }
    }

    public static void askUserId() {
        ConsoleView.printLine("Введите id пользователя:");
    }

    public static void printUserTransactions(Optional<List<Transaction>> transactions) {
        if (transactions.isEmpty()) {
            ConsoleView.printLine("У пользователя нет транзакций");
        } else {
            for (Transaction transaction : transactions.get()) {
                ConsoleView.printLine(transaction.toString());
            }
        }
    }
}
