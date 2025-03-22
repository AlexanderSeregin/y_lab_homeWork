package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.repository.NotificationRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of NotificationRepository
 */
public class PostgresNotificationRepository extends PostgresRepository<Notification, Long> implements NotificationRepository {
    private static final PostgresNotificationRepository INSTANCE = new PostgresNotificationRepository();

    private static final String SELECT_BY_ID = "SELECT * FROM finance_schema.notifications WHERE id = ? LIMIT 1";
    private static final String SELECT_ALL = "SELECT * FROM finance_schema.notifications";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM finance_schema.notifications WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO finance_schema.notifications (id, user_id, target_amount) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE finance_schema.notifications SET user_id = ?, name = ?, target_amount = ?, current_amount = ?, target_date = ?, description = ?, is_completed = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM finance_schema.notifications WHERE id = ?";


    private PostgresNotificationRepository() {
    }

    /**
     * Get singleton instance
     *
     * @return repository instance
     */
    public static PostgresNotificationRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return querySingle(SELECT_BY_ID, this::mapResultSetToNotification, id);
    }

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            Long notificationId = getNextSequenceValue("service_schema.notification_seq");
            notification.setId(notificationId);

            executeUpdate(INSERT,
                    notification.getId(),
                    notification.getUserId(),
                    notification.getMessage());
        } else {
            executeUpdate(UPDATE,
                    notification.getId(),
                    notification.getUserId(),
                    notification.getMessage());
        }
        return notification;
    }

    @Override
    public List<Notification> findAll() {
        return queryList(SELECT_ALL, this::mapResultSetToNotification);
    }

    @Override
    public void delete(Notification notification) {
        if (notification.getId() != null) {
            deleteById(notification.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        executeUpdate(DELETE, id);
    }

    @Override
    public Optional<Notification> findByUserId(Long userId) {
        return querySingle(SELECT_BY_USER_ID, this::mapResultSetToNotification, userId);
    }

    /**
     * Map database result to Notification entity
     *
     * @param rs ResultSet containing notification data
     * @return mapped Notification entity
     * @throws SQLException if mapping fails
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Long userId = rs.getLong("user_id");
        Notification notification = new Notification(
                rs.getLong("id"),
                rs.getString("message"),
                rs.getLong("user_id")
        );
        return notification;
    }
}
