package website.ylab.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(User user, String message) {
        notificationRepository.save(new Notification(user.getId(), message));
    }

    public Notification getNotification(Long userId) {
        return notificationRepository.findByUserId(userId).orElse(null);
    }
}
