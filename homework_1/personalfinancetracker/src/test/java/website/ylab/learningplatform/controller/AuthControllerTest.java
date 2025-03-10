package website.ylab.learningplatform.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.controller.AdminMenuController;
import website.ylab.learningplatform.controller.AuthController;
import website.ylab.learningplatform.controller.MenuController;
import website.ylab.learningplatform.controller.UserMenuController;
import website.ylab.learningplatform.datasource.TransactionDao;
import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.AdminService;
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.GoalService;
import website.ylab.learningplatform.service.TransactionService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.view.AdminMenuView;
import website.ylab.learningplatform.view.AuthView;
import website.ylab.learningplatform.view.ConsoleView;
import website.ylab.learningplatform.view.MenuView;
import website.ylab.learningplatform.view.TransactionView;
import website.ylab.learningplatform.view.UserView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private AuthView authView;

    @InjectMocks
    private AuthController authController;

//    @Test
//    void testRegister_Success() {
//        // Arrange
//        when(authView.askEmail()).thenReturn("test@test.com");
//        when(authView.askPassword()).thenReturn("password");
//        when(authView.askName()).thenReturn("Test User");
//        when(authService.register("Test User", "test@test.com", "password")).thenReturn(true);
//
//        // Act
//        authController.register();
//
//        // Assert
//        verify(authView).showRegistrationSuccess();
//        verify(authView, never()).showRegistrationErrorEmailAlreadyRegistered();
//    }
//
//    @Test
//    void testRegister_EmailAlreadyRegistered() {
//        // Arrange
//        when(authView.askEmail()).thenReturn("existing@test.com");
//        when(authView.askPassword()).thenReturn("password");
//        when(authView.askName()).thenReturn("Test User");
//        when(authService.register("Test User", "existing@test.com", "password")).thenReturn(false);
//
//        // Act
//        authController.register();
//
//        // Assert
//        verify(authView, never()).showRegistrationSuccess();
//        verify(authView).showRegistrationErrorEmailAlreadyRegistered();
//    }
//
//    @Test
//    void testLogin_Success_AsAdmin() {
//        try (MockedStatic<UserDao> userDaoMock = mockStatic(UserDao.class);
//             MockedStatic<AdminMenuController> adminMenuControllerMock = mockStatic(AdminMenuController.class)) {
//
//            // Arrange
//            String email = "admin@test.com";
//            String password = "password";
//            String hashedPassword = "hashedPassword";
//
//            when(authView.askEmail()).thenReturn(email);
//            when(authView.askPassword()).thenReturn(password);
//            when(authService.hashPassword(password)).thenReturn(hashedPassword);
//            when(authService.login(email, hashedPassword)).thenReturn(true);
//
//            UserDao userDaoInstance = mock(UserDao.class);
//            User adminUser = mock(User.class);
//            when(UserDao.getInstance()).thenReturn(userDaoInstance);
//            when(userDaoInstance.findByEmail(email)).thenReturn(adminUser);
//            when(adminUser.isAdmin()).thenReturn(true);
//
//            // Act
//            authController.login();
//
//            // Assert
//            verify(authView).showLoginSuccess();
//            verify(authView, never()).showLoginErrorWrongCredentials();
//            adminMenuControllerMock.verify(() -> AdminMenuController.showAdminMenu(), times(1));
//        }
//    }
//
//    @Test
//    void testLogin_Success_AsRegularUser() {
//        try (MockedStatic<UserDao> userDaoMock = mockStatic(UserDao.class);
//             MockedStatic<UserMenuController> userMenuControllerMock = mockStatic(UserMenuController.class)) {
//
//            // Arrange
//            String email = "user@test.com";
//            String password = "password";
//            String hashedPassword = "hashedPassword";
//
//            when(authView.askEmail()).thenReturn(email);
//            when(authView.askPassword()).thenReturn(password);
//            //???when(authService.hashPassword(password)).thenReturn(hashedPassword);
//            when(authService.login(email, hashedPassword)).thenReturn(true);
//
//            UserDao userDaoInstance = mock(UserDao.class);
//            User regularUser = mock(User.class);
//            when(UserDao.getInstance()).thenReturn(userDaoInstance);
//            when(userDaoInstance.findByEmail(email)).thenReturn(regularUser);
//            when(regularUser.isAdmin()).thenReturn(false);
//
//            // Act
//            authController.login();
//
//            // Assert
//            verify(authView).showLoginSuccess();
//            verify(authView, never()).showLoginErrorWrongCredentials();
//            userMenuControllerMock.verify(() -> UserMenuController.showUserMenu(regularUser), times(1));
//        }
//    }
//
//    @Test
//    void testLogin_Failure() {
//        // Arrange
//        String email = "wrong@test.com";
//        String password = "wrongPassword";
//        String hashedPassword = "hashedWrongPassword";
//
//        when(authView.askEmail()).thenReturn(email);
//        when(authView.askPassword()).thenReturn(password);
//        when(authService.hashPassword(password)).thenReturn(hashedPassword);
//        when(authService.login(email, hashedPassword)).thenReturn(false);
//
//        // Act
//        authController.login();
//
//        // Assert
//        verify(authView, never()).showLoginSuccess();
//        verify(authView).showLoginErrorWrongCredentials();
//    }
}