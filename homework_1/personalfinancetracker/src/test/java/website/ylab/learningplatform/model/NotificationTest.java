package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {


    @Test
    void testConstructorWithUserIdAndMessage() {
        // Given
        String expectedMessage = "Test notification message";
        long expectedUserId = 123L;

        // When
        Notification notification = new Notification(expectedUserId, expectedMessage);

        // Then
        assertEquals(expectedMessage, notification.getMessage());
        assertEquals(expectedUserId, notification.getId());
    }

    @Test
    void testGetMessage() {
        // Given
        String expectedMessage = "Test notification message";
        Notification notification = new Notification(expectedMessage);

        // When
        String actualMessage = notification.getMessage();

        // Then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetId() {
        // Given
        long expectedUserId = 456L;
        Notification notification = new Notification(expectedUserId, "Test message");

        // When
        Long actualUserId = notification.getId();

        // Then
        assertEquals(expectedUserId, actualUserId);
    }

}