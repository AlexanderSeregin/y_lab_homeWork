package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.UserDao;


public class AdminService {
    public static void blockUser(long userId) {
        UserDao.getInstance().get(userId).ifPresent(user -> user.setIsBlocked(true));
    }
}
