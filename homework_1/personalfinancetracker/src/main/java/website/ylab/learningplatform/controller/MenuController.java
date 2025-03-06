package website.ylab.learningplatform.controller;

import website.ylab.learningplatform.view.ConsoleView;
import website.ylab.learningplatform.view.MenuView;

public class MenuController {
    private static final AuthController authController = new AuthController();


    public static void runMenu() {
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
                    ConsoleView.printLine("Спасибо за использование!");
                    System.exit(0);
                    break;
                default:
                    ConsoleView.printError("Ошибка выбора. Попробуйте ещё раз.");
            }
    }

}}
