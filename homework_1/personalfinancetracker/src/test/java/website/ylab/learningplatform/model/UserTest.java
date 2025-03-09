package website.ylab.learningplatform.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class UserTest {

    @Test
    void testConstructorWithBasicParameters() {
        // Arrange
        String name = "John Doe";
        String email = "john.doe@example.com";
        String passwordHash = "hashedPassword123";

        // Act
        User user = new User(name, email, passwordHash);

        // Assert
        assertNotEquals(0, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertFalse(user.isAdmin());
        assertFalse(user.getIsBlocked());
        assertEquals(new BigDecimal(0), user.getBalance());
    }

    @Test
    void testConstructorWithAllParameters() {
        // Arrange
        String name = "Admin User";
        String email = "admin@example.com";
        String passwordHash = "adminHashedPassword123";
        boolean isAdmin = true;
        boolean isBlocked = false;
        BigDecimal balance = new BigDecimal("100.50");

        // Act
        User user = new User(name, email, passwordHash, isAdmin, isBlocked, balance);

        // Assert
        assertNotEquals(0, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertTrue(user.isAdmin());
        assertFalse(user.getIsBlocked());
        assertEquals(balance, user.getBalance());
    }

    @Test
    void testIdIncrementsWithNewUsers() {
        // Act
        User user1 = new User("User1", "user1@example.com", "hash1");
        User user2 = new User("User2", "user2@example.com", "hash2");

        // Assert
        assertTrue(user2.getId() > user1.getId());
    }

    @Test
    void testSetName() {
        // Arrange
        User user = new User("Initial Name", "email@example.com", "hash");
        String newName = "Updated Name";

        // Act
        user.setName(newName);

        // Assert
        assertEquals(newName, user.getName());
    }

    @Test
    void testSetEmail() {
        // Arrange
        User user = new User("Test User", "initial@example.com", "hash");
        String newEmail = "updated@example.com";

        // Act
        user.setEmail(newEmail);

        // Assert
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void testSetPasswordHash() {
        // Arrange
        User user = new User("Test User", "email@example.com", "initialHash");
        String newPasswordHash = "updatedHash";

        // Act
        user.setPasswordHash(newPasswordHash);

        // Assert
        assertEquals(newPasswordHash, user.getPasswordHash());
    }

    @Test
    void testSetBalance() {
        // Arrange
        User user = new User("Test User", "email@example.com", "hash");
        BigDecimal newBalance = new BigDecimal("250.75");

        // Act
        user.setBalance(newBalance);

        // Assert
        assertEquals(newBalance, user.getBalance());
    }


    @Test
    void testSetId() {
        // Arrange
        User user = new User("Test User", "email@example.com", "hash");
        long newId = 1000L;

        // Act
        user.setId(newId);

        // Assert
        assertEquals(newId, user.getId());
    }
}