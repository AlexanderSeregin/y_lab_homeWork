package website.ylab.learningplatform.repository;

import website.ylab.learningplatform.model.Notification;

import java.util.Optional;

/**
 * Repository interface for Goal entity
 */
public interface NotificationRepository extends Repository<Notification, Long> {
    /**
     * Find goal by user ID
     *
     * @param userId user ID
     * @return Optional containing goal if found
     */
    Optional<Notification> findByUserId(Long userId);
}
