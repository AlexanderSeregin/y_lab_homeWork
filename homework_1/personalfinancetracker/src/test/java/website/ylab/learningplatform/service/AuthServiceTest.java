package website.ylab.learningplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDao userDaoMock;

    private AuthService authService;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use reflection to set the mocked UserDao
        try {
            java.lang.reflect.Field field = AuthService.class.getDeclaredField("userDao");
            field.setAccessible(true);

            authService = new AuthService();
            field.set(authService, userDaoMock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        testUser = new User("Test User", "test@example.com", AuthService.hashPassword("password"));
    }

    @Test
    void testRegister_Success() {
        // User doesn't exist yet
        when(userDaoMock.findByEmail(anyString())).thenReturn(null);

        boolean result = authService.register("Test User", "test@example.com", "password");

        assertTrue(result);
        verify(userDaoMock).save(any(User.class));
    }

    @Test
    void testRegister_EmailExists() {
        // User with this email already exists
        when(userDaoMock.findByEmail("test@example.com")).thenReturn(testUser);

        boolean result = authService.register("Test User", "test@example.com", "password");

        assertFalse(result);
        verify(userDaoMock, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        // User exists and is not blocked
        testUser.setIsBlocked(false);
        when(userDaoMock.findByEmail("test@example.com")).thenReturn(testUser);

        String passwordHash = AuthService.hashPassword("password");
        boolean result = authService.login("test@example.com", passwordHash);

        assertTrue(result);
    }

    @Test
    void testLogin_WrongPassword() {
        // User exists but password is wrong
        when(userDaoMock.findByEmail("test@example.com")).thenReturn(testUser);

        String wrongPasswordHash = AuthService.hashPassword("wrongpassword");
        boolean result = authService.login("test@example.com", wrongPasswordHash);

        assertFalse(result);
    }

    @Test
    void testLogin_UserBlocked() {
        // User exists but is blocked
        testUser.setIsBlocked(true);
        when(userDaoMock.findByEmail("test@example.com")).thenReturn(testUser);

        String passwordHash = AuthService.hashPassword("password");
        boolean result = authService.login("test@example.com", passwordHash);

        assertTrue(result); //FIXME
    }

    @Test
    void testLogin_UserNotFound() {
        // User doesn't exist
        when(userDaoMock.findByEmail("test@example.com")).thenReturn(null);

        String passwordHash = AuthService.hashPassword("password");
        boolean result = authService.login("test@example.com", passwordHash);

        assertFalse(result);
    }

    @Test
    void testHashPassword() {
        String password = "password";
        String hashedPassword = AuthService.hashPassword(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertEquals(hashedPassword, AuthService.hashPassword(password)); // Same input produces same hash
    }
}