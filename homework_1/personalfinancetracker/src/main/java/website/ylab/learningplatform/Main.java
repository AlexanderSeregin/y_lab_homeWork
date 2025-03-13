package website.ylab.learningplatform;


import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.datasource.*;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.controller.*;
import website.ylab.learningplatform.service.AuthService;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("classpath = " + System.getProperty("java.class.path"));



        UserDao userDao = UserDao.getInstance();
        userDao.save(new User("admin", "admin@admin.com", AuthService.hashPassword("admin"), true, false, BigDecimal.valueOf(0)));
        userDao.save(new User("user", "user@user.com", AuthService.hashPassword("user"), false, false, BigDecimal.valueOf(1000)));

        LiquibaseConfig.getInstance().migrate();

        MenuController.runMainMenu();

    }
}