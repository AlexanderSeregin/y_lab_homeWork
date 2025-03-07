package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.NotificationDao;
import website.ylab.learningplatform.model.User;

public class NotificationService {
    public static void sendNotification(User user, String message) {
        NotificationDao.getInstance().save(new website.ylab.learningplatform.model.Notification(user.getId(), message));
    }
}
