package website.ylab.learningplatform.controller;

import website.ylab.learningplatform.datasource.NotificationDao;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.view.ConsoleView;
import website.ylab.learningplatform.view.UserView;

public class UserController {
    public static void run(User user) {
        UserView.printNotification(user);
        while (true) {
            UserView.printMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    userMenuSelfManagement(User);
                    break;
                case 2:
                    userMenuTransaction(User);
                    break;
                case 3:
                    userMenuBudget(User);
                    break;
                case 4:
                    userMenuGoal(User);
                    break;
                case 5:
                    userMenuStats(User);
                    break;
                case 9:
                    MenuController.runMenu();
                case 0:
                    ConsoleView.printLine("Спасибо за использование!");
                    System.exit(0);
                    break;
                default:
                    ConsoleView.printError("Ошибка выбора. Попробуйте ещё раз.");
            }

        }
    }
    public static void userMenuSelfManagement(User user) {
        while (true) {
            UserView.printSelfManagementMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    String newName = ConsoleView.readString();
                    UserService.userChangeName(user, newName);
                    break;
                case 2:
                    String newEmail = ConsoleView.readString();
                    UserService.userChangeEmail(user, newEmail);

                    break;
                case 3:
                    String newPassword = ConsoleView.readString();
                    UserService.userChangePassword(user, newPassword);
                    break;
                case 4:
                    UserService.userDelete(user);
                    break;
                case 0:
                    return;
                default:
                    ConsoleView.printError("Ошибка выбора. Попробуйте ещё раз.");
            }
        }
    }
    public static void userMenuTransaction(User user) {
        while (true) {
            UserView.printTransactionMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    String newName = ConsoleView.readString();
                    UserService.userChangeName(user, newName);
                    break;
                case 2:
                    String newEmail = ConsoleView.readString();
                    UserService.userChangeEmail(user, newEmail);

                    break;
                case 3:
                    String newPassword = ConsoleView.readString();
                    UserService.userChangePassword(user, newPassword);
                    break;
                case 4:
                    UserService.userDelete(user);
                    break;
                case 0:
                    return;
                default:
                    ConsoleView.printError("Ошибка выбора. Попробуйте ещё раз.");
            }
    }
}