package website.ylab.learningplatform;


import website.ylab.learningplatform.datasource.*;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.controller.*;
import website.ylab.learningplatform.service.AuthService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        UserDao userDao = UserDao.getInstance();
        userDao.save(new User("admin", "admin@admin.com", AuthService.hashPassword("admin"), true, false, BigDecimal.valueOf(0)));
        userDao.save(new User("user", "user@user.com", AuthService.hashPassword("user"), false, false, BigDecimal.valueOf(1000)));

        MenuController.runMainMenu();

    }
}