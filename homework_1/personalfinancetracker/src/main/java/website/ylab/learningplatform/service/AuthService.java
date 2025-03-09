package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.User;

import java.util.Base64;

public class AuthService {
    private UserDao userDao = UserDao.getInstance();

    public AuthService() {
    }

    public boolean register(String name, String email, String password) {
        if (userDao.findByEmail(email) != null) {
            return false;
        }
        User newUser = new User(name, email, hashPassword(password));
        userDao.save(newUser);
        return true;
    }

    public boolean login(String email, String passwordHash) {
        User user = userDao.findByEmail(email);
        return user != null && user.getPasswordHash().equals(passwordHash) && !user.getIsBlocked();
    }

    public static String hashPassword(String password) {
        return Base64.getEncoder().encodeToString(("salt" + password).getBytes());
    }
}
