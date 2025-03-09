package website.ylab.learningplatform.service;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.User;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    private UserDao userDaoMock;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userDaoMock = mock(UserDao.class);
        testUser = new User("Test User", "test@example.com", "hashedPassword");
    }

//    @Test
//    public void blockUser_WhenUserExists_ShouldBlockUser() {
//        // Arrange
//        long userId = 1L;
//        testUser.setId(userId);
//        when(userDaoMock.get(userId)).thenReturn(Optional.of(testUser));
//
//        try (MockedStatic<UserDao> mockedStatic = mockStatic(UserDao.class)) {
//            // Mock the static getInstance method
//            mockedStatic.when(UserDao::getInstance).thenReturn(userDaoMock);
//
//            // Act
//            AdminService.blockUser(userId);
//
//            // Assert
//            verify(userDaoMock).get(userId);
//            assert testUser.getIsBlocked(); // verify user is blocked
//        }
//    }

    @Test
    public void blockUser_WhenUserDoesNotExist_ShouldDoNothing() {
        // Arrange
        long userId = 999L;
        when(userDaoMock.get(userId)).thenReturn(Optional.empty());

        try (MockedStatic<UserDao> mockedStatic = mockStatic(UserDao.class)) {
            // Mock the static getInstance method
            mockedStatic.when(UserDao::getInstance).thenReturn(userDaoMock);

            // Act
            AdminService.blockUser(userId);

            // Assert
            verify(userDaoMock).get(userId);
            // No additional actions should be taken
        }
    }

//    @Test
//    public void blockUser_WhenUserAlreadyBlocked_ShouldKeepBlocked() {
//        // Arrange
//        long userId = 1L;
//        testUser.setId(userId);
//        testUser.setIsBlocked(true); // User is already blocked
//        when(userDaoMock.get(userId)).thenReturn(Optional.of(testUser));
//
//        try (MockedStatic<UserDao> mockedStatic = mockStatic(UserDao.class)) {
//            // Mock the static getInstance method
//            mockedStatic.when(UserDao::getInstance).thenReturn(userDaoMock);
//
//            // Act
//            AdminService.blockUser(userId);
//
//            // Assert
//            verify(userDaoMock).get(userId);
//            assert testUser.getIsBlocked(); // verify user remains blocked
//        }
//    }
}