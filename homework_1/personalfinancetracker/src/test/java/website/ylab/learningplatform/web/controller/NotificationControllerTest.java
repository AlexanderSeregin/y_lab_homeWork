package website.ylab.learningplatform.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.NotificationService;
import website.ylab.learningplatform.service.UserService;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationController notificationController;

    private User regularUser;
    private User adminUser;
    private User targetUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        // Setup regular user
        regularUser = new User();
        regularUser.setId(1L);
        regularUser.setEmail("user@example.com");
        regularUser.setBalance(new BigDecimal("1000.00"));
        regularUser.setAdmin(false);

        // Setup admin user
        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setEmail("admin@example.com");
        adminUser.setBalance(new BigDecimal("2000.00"));
        adminUser.setAdmin(true);

        // Setup target user
        targetUser = new User();
        targetUser.setId(3L);
        targetUser.setEmail("target@example.com");
        targetUser.setBalance(new BigDecimal("500.00"));
        targetUser.setAdmin(false);

        // Setup test notification
        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setUserId(1L);
        testNotification.setMessage("Test notification message");
    }

    @Test
    void getUserNotifications_UserExistsWithNotifications_ReturnsNotifications() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(regularUser);
        when(notificationService.getNotification(1L)).thenReturn(testNotification);

        // Act
        ResponseEntity<?> response = notificationController.getUserNotifications(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(Notification.class, response.getBody());
        Notification returnedNotification = (Notification) response.getBody();
        assertEquals(testNotification.getId(), returnedNotification.getId());
        assertEquals(testNotification.getMessage(), returnedNotification.getMessage());
    }

    @Test
    void getUserNotifications_UserExistsNoNotifications_ReturnsMessage() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(regularUser);
        when(notificationService.getNotification(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = notificationController.getUserNotifications(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No notifications", response.getBody());
    }

    @Test
    void getUserNotifications_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = notificationController.getUserNotifications(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(notificationService, never()).getNotification(anyLong());
    }

    @Test
    void createNotification_AdminUser_CreatesNotification() {
        // Arrange
        String message = "Important notification";
        when(userService.getUserById(2L)).thenReturn(adminUser);
        when(userService.getUserById(3L)).thenReturn(targetUser);

        // Act
        ResponseEntity<?> response = notificationController.createNotification(3L, message, 2L);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Notification sent", response.getBody());
        verify(notificationService).sendNotification(targetUser, message);
    }

    @Test
    void createNotification_NonAdminUser_ReturnsForbidden() {
        // Arrange
        String message = "Important notification";
        when(userService.getUserById(1L)).thenReturn(regularUser);

        // Act
        ResponseEntity<?> response = notificationController.createNotification(3L, message, 1L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Only admins can create notifications", response.getBody());
        verify(notificationService, never()).sendNotification(any(), anyString());
    }

    @Test
    void createNotification_AdminUserTargetNotFound_ReturnsNotFound() {
        // Arrange
        String message = "Important notification";
        when(userService.getUserById(2L)).thenReturn(adminUser);
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = notificationController.createNotification(999L, message, 2L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Target user not found", response.getBody());
        verify(notificationService, never()).sendNotification(any(), anyString());
    }

    @Test
    void createNotification_UserNotFound_ReturnsNotFound() {
        // Arrange
        String message = "Important notification";
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = notificationController.createNotification(3L, message, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, never()).getUserById(3L);
        verify(notificationService, never()).sendNotification(any(), anyString());
    }
}
