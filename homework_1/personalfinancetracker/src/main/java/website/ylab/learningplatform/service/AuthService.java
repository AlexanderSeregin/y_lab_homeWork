package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.User;

public class AuthService {
    private UserDao userDao = UserDao.getInstance();

    public AuthService() {
    }

    public boolean register(String name, String email, String password) {
        if (userDao.findByEmail(email) != null) {
            return false;
        }
        User newUser = new User(name, email, password);
        userDao.save(newUser);
        return true;
    }

    public boolean login(String email, String password) {
        User user = userDao.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
