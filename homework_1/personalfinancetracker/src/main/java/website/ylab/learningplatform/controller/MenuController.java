package website.ylab.learningplatform.controller;

import website.ylab.learningplatform.view.ConsoleView;
import website.ylab.learningplatform.view.MenuView;

public class MenuController {
    private static final AuthController authController = new AuthController();


    /**
     * Shows the main menu to the user and waits for input.
     * <p>
     * The menu is shown in an infinite loop until the user chooses to exit (0).
     * If the user chooses to login (1), the user is asked for credentials and
     * if they are correct, the user's menu is shown. If the user chooses to
     * register (2), the user is asked for credentials and if the email is not
     * already registered, the user is added to the database.
     */
    public static void runMainMenu() {
        while (true) {
            MenuView.printStartMenu();
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    authController.login();
                    break;
                case 2:
                    authController.register();
                    break;
                case 0:
                    ConsoleView.printBye();
                    System.exit(0);
                    break;
                default:
                    ConsoleView.printErrorSelection();
            }
        }

    }
}
