package website.ylab.learningplatform;


import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.controller.MenuController;

public class Main {
    public static void main(String[] args) {
        LiquibaseConfig.getInstance().migrate();

        MenuController.runMainMenu();

    }
}