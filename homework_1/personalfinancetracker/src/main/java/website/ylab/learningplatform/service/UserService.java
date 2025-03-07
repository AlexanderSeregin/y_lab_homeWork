package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.User;

public class UserService {
    public static void userChangeEmail(User user, String newEmail) {
        user.setEmail(newEmail);
    }

    public static void userChangeName(User user, String newName) {
        user.setName(newName);
    }

    public static void userChangePassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    public static void userDelete(User user) {
        UserDao.getInstance().delete(user);
    }
}
