package website.ylab.learningplatform.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import website.ylab.learningplatform.model.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDaoTest {

    private NotificationDao notificationDao;
    private Notification testNotification;
    private final long testUserId = 1L;

    @BeforeEach
    void setUp() {
        notificationDao = NotificationDao.getInstance();
        // Clear any existing data by using reflection to reset the repository
        try {
            java.lang.reflect.Field field = NotificationDao.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(notificationDao, new java.util.HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        testNotification = new Notification(testUserId, "Test notification message");
    }

    @Test
    void testSaveAndGet() {
        notificationDao.save(testNotification);

        Optional<Notification> result = notificationDao.get(testUserId);

        assertTrue(result.isPresent());
        assertEquals(testNotification.getMessage(), result.get().getMessage());
    }

    @Test
    void testDelete() {
        notificationDao.save(testNotification);
        notificationDao.delete(testNotification);

        Optional<Notification> result = notificationDao.get(testUserId);

        assertFalse(result.isPresent());
    }

    @Test
    void testMultipleNotifications() {
        notificationDao.save(testNotification);

        Notification secondNotification = new Notification(testUserId, "Second notification");
        notificationDao.save(secondNotification);

        // First notification should be returned first
        Optional<Notification> firstResult = notificationDao.get(testUserId);
        assertTrue(firstResult.isPresent());
        assertEquals(testNotification.getMessage(), firstResult.get().getMessage());

        // Second notification should be returned next
        Optional<Notification> secondResult = notificationDao.get(testUserId);
        assertTrue(secondResult.isPresent());
        assertEquals(secondNotification.getMessage(), secondResult.get().getMessage());

        // No more notifications should be available
        Optional<Notification> thirdResult = notificationDao.get(testUserId);
        assertFalse(thirdResult.isPresent());
    }

    @Test
    void testGetAll() {
        notificationDao.save(testNotification);

        Notification secondNotification = new Notification(2L, "Notification for another user");
        notificationDao.save(secondNotification);

        Iterable<Notification> notifications = notificationDao.getAll();
        List<Notification> notificationList = new ArrayList<>();
        notifications.forEach(notificationList::add);

        assertEquals(2, notificationList.size());

        boolean foundTestNotification = false;
        boolean foundSecondNotification = false;

        for (Notification notification : notificationList) {
            if (notification.getId() == testUserId && notification.getMessage().equals(testNotification.getMessage())) {
                foundTestNotification = true;
            } else if (notification.getId() == 2L && notification.getMessage().equals(secondNotification.getMessage())) {
                foundSecondNotification = true;
            }
        }

        assertTrue(foundTestNotification);
        assertTrue(foundSecondNotification);
    }
}