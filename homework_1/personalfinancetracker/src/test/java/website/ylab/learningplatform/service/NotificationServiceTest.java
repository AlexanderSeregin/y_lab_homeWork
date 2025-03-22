package website.ylab.learningplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import website.ylab.learningplatform.datasource.NotificationDao;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationDao mockDao;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockDao = mock(NotificationDao.class);
        testUser = new User("Test User", "test@example.com", "hashedPassword");
    }

    @Test
    void testSendNotification() {
        // Arrange
        String testMessage = "Test notification message";

        try (MockedStatic<NotificationDao> mockedStatic = Mockito.mockStatic(NotificationDao.class)) {
            mockedStatic.when(NotificationDao::getInstance).thenReturn(mockDao);

            // Act
            NotificationService.sendNotification(testUser, testMessage);

            // Assert
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
            verify(mockDao).save(notificationCaptor.capture());

            Notification capturedNotification = notificationCaptor.getValue();
            assertEquals(testUser.getId(), capturedNotification.getId());
            assertEquals(testMessage, capturedNotification.getMessage());
        }
    }

    @Test
    void testSendNotificationWithAdminUser() {
        // Arrange
        String testMessage = "Admin notification";
        User adminUser = new User("Admin", "admin@example.com", "adminPass", true, false, new BigDecimal("100"));

        try (MockedStatic<NotificationDao> mockedStatic = Mockito.mockStatic(NotificationDao.class)) {
            mockedStatic.when(NotificationDao::getInstance).thenReturn(mockDao);

            // Act
            NotificationService.sendNotification(adminUser, testMessage);

            // Assert
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
            verify(mockDao).save(notificationCaptor.capture());

            Notification capturedNotification = notificationCaptor.getValue();
            assertEquals(adminUser.getId(), capturedNotification.getId());
            assertEquals(testMessage, capturedNotification.getMessage());
        }
    }

    @Test
    void testSendNotificationWithBlockedUser() {
        // Arrange
        String testMessage = "Blocked user notification";
        User blockedUser = new User("Blocked", "blocked@example.com", "blockedPass", false, true, new BigDecimal("0"));

        try (MockedStatic<NotificationDao> mockedStatic = Mockito.mockStatic(NotificationDao.class)) {
            mockedStatic.when(NotificationDao::getInstance).thenReturn(mockDao);

            // Act
            NotificationService.sendNotification(blockedUser, testMessage);

            // Assert
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
            verify(mockDao).save(notificationCaptor.capture());

            Notification capturedNotification = notificationCaptor.getValue();
            assertEquals(blockedUser.getId(), capturedNotification.getId());
            assertEquals(testMessage, capturedNotification.getMessage());
        }
    }

    @Test
    void testSendEmptyNotification() {
        // Arrange
        String emptyMessage = "";

        try (MockedStatic<NotificationDao> mockedStatic = Mockito.mockStatic(NotificationDao.class)) {
            mockedStatic.when(NotificationDao::getInstance).thenReturn(mockDao);

            // Act
            NotificationService.sendNotification(testUser, emptyMessage);

            // Assert
            ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
            verify(mockDao).save(notificationCaptor.capture());

            Notification capturedNotification = notificationCaptor.getValue();
            assertEquals(testUser.getId(), capturedNotification.getId());
            assertEquals(emptyMessage, capturedNotification.getMessage());
        }
    }
}