package website.ylab.learningplatform.controller;


import website.ylab.learningplatform.repository.impl.PostgresTransactionRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;
import website.ylab.learningplatform.service.AdminService;
import website.ylab.learningplatform.view.AdminMenuView;
import website.ylab.learningplatform.view.ConsoleView;


public class AdminMenuController {

    public static void showAdminMenu() {
        while (true) {
            AdminMenuView.printAdminMenu();
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    AdminMenuView.printUsers(PostgresUserRepository.getInstance().findAll());
                    break;
                case 2:
                    AdminMenuView.askUserId();
                    long userId = ConsoleView.readLong();
                    AdminMenuView.printUserTransactions(PostgresTransactionRepository.getInstance().findByUserId(userId));
                    break;
                case 3:
                    AdminMenuView.askUserId();
                    long id = ConsoleView.readLong();
                    AdminService.blockUser(id);
                    break;
                case 9:
                    MenuController.runMainMenu();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    ConsoleView.printErrorSelection();
            }
        }
    }
}
