package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.NotificationRepository;
import website.ylab.learningplatform.repository.impl.PostgresNotificationRepository;

public class NotificationService {
    private static final NotificationRepository notificationRepository = PostgresNotificationRepository.getInstance();

    public static void sendNotification(User user, String message) {
        notificationRepository.save(new Notification(user.getId(), message));
    }

    public static Notification getNotification(User user) {
        return notificationRepository.findByUserId(user.getId()).orElse(null);
    }
}
