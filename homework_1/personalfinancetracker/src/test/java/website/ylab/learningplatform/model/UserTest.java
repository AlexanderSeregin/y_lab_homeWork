package website.ylab.learningplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@test.com", "hashedPassword");
    }

    @Test
    void constructor_WithDefaultValues_ShouldSetCorrectly() {
        assertNotNull(user.getId());
        assertEquals("Test User", user.getUsername());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertFalse(user.isAdmin());
        assertFalse(user.isBlocked());
        assertEquals(BigDecimal.ZERO, user.getBalance());
    }

    @Test
    void constructor_WithAllParameters_ShouldSetCorrectly() {
        User customUser = new User("Custom User", "custom@test.com", "customPassword", true, true, BigDecimal.valueOf(1000));

        assertNotNull(customUser.getId());
        assertEquals("Custom User", customUser.getUsername());
        assertEquals("custom@test.com", customUser.getEmail());
        assertEquals("customPassword", customUser.getPasswordHash());
        assertTrue(customUser.isAdmin());
        assertTrue(customUser.isBlocked());
        assertEquals(BigDecimal.valueOf(1000), customUser.getBalance());
    }

    @Test
    void hasEnoughBalance_WithSufficientFunds_ShouldReturnTrue() {
        user.setBalance(BigDecimal.valueOf(1000));
        assertTrue(user.hasEnoughBalance(BigDecimal.valueOf(500)));
    }

    @Test
    void hasEnoughBalance_WithInsufficientFunds_ShouldReturnFalse() {
        user.setBalance(BigDecimal.valueOf(100));
        assertFalse(user.hasEnoughBalance(BigDecimal.valueOf(500)));
    }

    @Test
    void hasEnoughBalance_WithEqualAmount_ShouldReturnTrue() {
        user.setBalance(BigDecimal.valueOf(500));
        assertTrue(user.hasEnoughBalance(BigDecimal.valueOf(500)));
    }

    @Test
    void updateBalance_WithValidDeposit_ShouldIncreaseBalance() {
        user.setBalance(BigDecimal.valueOf(1000));
        user.updateBalance(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(1500), user.getBalance());
    }

    @Test
    void updateBalance_WithValidWithdrawal_ShouldDecreaseBalance() {
        user.setBalance(BigDecimal.valueOf(1000));
        user.updateBalance(BigDecimal.valueOf(-500));
        assertEquals(BigDecimal.valueOf(500), user.getBalance());
    }

    @Test
    void updateBalance_WithInsufficientFunds_ShouldThrowException() {
        user.setBalance(BigDecimal.valueOf(100));
        assertThrows(IllegalArgumentException.class, () ->
                user.updateBalance(BigDecimal.valueOf(-500))
        );
    }

    @Test
    void setEmail_ShouldUpdateEmail() {
        String newEmail = "newemail@test.com";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void setUsername_ShouldUpdateUsername() {
        String newUsername = "New Test User";
        user.setUsername(newUsername);
        assertEquals(newUsername, user.getUsername());
    }

    @Test
    void setPassword_ShouldUpdatePasswordHash() {
        String newPassword = "newHashedPassword";
        user.setPassword(newPassword);
        assertEquals(newPassword, user.getPasswordHash());
    }

    @Test
    void setBlocked_ShouldUpdateBlockedStatus() {
        assertFalse(user.isBlocked());
        user.setBlocked(true);
        assertTrue(user.isBlocked());
    }

    @Test
    void getName_ShouldReturnUsername() {
        assertEquals(user.getUsername(), user.getName());
    }

    @Test
    void setName_ShouldUpdateUsername() {
        String newName = "New Name";
        user.setName(newName);
        assertEquals(newName, user.getUsername());
        assertEquals(newName, user.getName());
    }
}
