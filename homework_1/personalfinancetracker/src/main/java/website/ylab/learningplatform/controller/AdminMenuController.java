package website.ylab.learningplatform.controller;

import website.ylab.learningplatform.datasource.TransactionDao;
import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.service.AdminService;
import website.ylab.learningplatform.view.AdminMenuView;
import website.ylab.learningplatform.view.ConsoleView;


public class AdminMenuController {
    /**
     * Show admin menu
     *
     * <p>This method displays an admin menu. The menu allows the administrator to view the list of users,
     * view the transactions of the user, block the user, or exit the program.</p>
     *
     * <p>It uses the following methods:</p>
     *
     * <ul>
     * <li>{@link website.ylab.learningplatform.view.AdminMenuView#printAdminMenu()}</li>
     * <li>{@link website.ylab.learningplatform.view.ConsoleView#readInt()}</li>
     * <li>{@link website.ylab.learningplatform.datasource.UserDao#getAll()}</li>
     * <li>{@link website.ylab.learningplatform.datasource.TransactionDao#get(long)}</li>
     * <li>{@link website.ylab.learningplatform.service.AdminService#blockUser(long)}</li>
     * <li>{@link website.ylab.learningplatform.controller.MenuController#runMainMenu()}</li>
     * <li>{@link System#exit(int)}</li>
     * </ul>
     *
     * <p>It also uses the following constants:</p>
     *
     * <ul>
     * <li>{@link website.ylab.learningplatform.view.ConsoleView#ERROR_SELECTION}</li>
     * </ul>
     *
     * @since 1.0
     */
    public static void showAdminMenu() {
        while (true) {
            AdminMenuView.printAdminMenu();
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    AdminMenuView.printUsers(UserDao.getInstance().getAll());
                    break;
                case 2:
                    AdminMenuView.askUserId();
                    long userId = ConsoleView.readLong();
                    AdminMenuView.printUserTransactions(TransactionDao.getInstance().get(userId));
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
