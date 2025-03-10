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

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserMenuControllerTest {

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        //???when(mockUser.getId()).thenReturn(1L);
    }

//    @Test
//    void testShowUserMenu_SelfManagement() {
//        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
//             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
//             MockedStatic<UserMenuController> userMenuControllerMock = Mockito.mockStatic(UserMenuController.class,
//                     invocation -> {
//                         if (invocation.getMethod().getName().equals("userMenuSelfManagement")) {
//                             return null; // Do nothing for this method
//                         }
//                         return invocation.callRealMethod();
//                     })) {
//
//            // Arrange
//            when(ConsoleView.readInt())
//                    .thenReturn(1)  // Self management
//                    .thenReturn(0); // Exit
//
//            // Act
//            try {
//                UserMenuController.showUserMenu(mockUser);
//            } catch (Exception e) {
//                // Expected as this is a mock
//            }
//
//            // Assert
//            userViewMock.verify(() -> UserView.printNotification(mockUser), times(1));
//            userViewMock.verify(() -> UserView.printMenu(mockUser), times(2));
//            userMenuControllerMock.verify(() -> UserMenuController.userMenuSelfManagement(mockUser), times(1));
//        }
//    }

//    @Test
//    void testShowUserMenu_Transaction() {
//        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
//             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
//             MockedStatic<UserMenuController> userMenuControllerMock = Mockito.mockStatic(UserMenuController.class,
//                     invocation -> {
//                         if (invocation.getMethod().getName().equals("userMenuTransaction")) {
//                             return null; // Do nothing for this method
//                         }
//                         return invocation.callRealMethod();
//                     })) {
//
//            // Arrange
//            when(ConsoleView.readInt())
//                    .thenReturn(2)  // Transaction
//                    .thenReturn(0); // Exit
//
//            // Act
//            try {
//                UserMenuController.showUserMenu(mockUser);
//            } catch (Exception e) {
//                // Expected as this is a mock
//            }
//
//            // Assert
//            userViewMock.verify(() -> UserView.printMenu(mockUser), times(2));
//            userMenuControllerMock.verify(() -> UserMenuController.userMenuTransaction(mockUser), times(1));
//        }
//    }

    @Test
    void testUserMenuSelfManagement_ChangeName() {
        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
             MockedStatic<UserService> userServiceMock = mockStatic(UserService.class)) {

            // Arrange
            when(ConsoleView.readInt())
                    .thenReturn(1)  // Change name
                    .thenReturn(0); // Back
            when(ConsoleView.readString()).thenReturn("New Name");

            // Act
            UserMenuController.userMenuSelfManagement(mockUser);

            // Assert
            userViewMock.verify(() -> UserView.printSelfManagementMenu(mockUser), times(2));
            userServiceMock.verify(() -> UserService.userChangeName(mockUser, "New Name"), times(1));
        }
    }

//    @Test
//    void testUserMenuTransaction_AddTransaction() {
//        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
//             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
//             MockedStatic<TransactionService> transactionServiceMock = mockStatic(TransactionService.class);
//             MockedStatic<UserMenuController> userMenuControllerMock = Mockito.mockStatic(UserMenuController.class,
//                     invocation -> {
//                         if (invocation.getMethod().getName().equals("showUserMenu")) {
//                             return null; // Do nothing for this method
//                         }
//                         return invocation.callRealMethod();
//                     })) {
//
//            // Arrange
//            when(ConsoleView.readInt()).thenReturn(1); // Add transaction
//            when(ConsoleView.readBool()).thenReturn(true); // Is income
//            when(ConsoleView.readString()).thenReturn("Salary");
//            when(ConsoleView.readBigDecimal()).thenReturn(new BigDecimal("1000.00"));
//
//            // Act
//            UserMenuController.userMenuTransaction(mockUser);
//
//            // Assert
//            userViewMock.verify(() -> UserView.printTransactionMenu(mockUser), times(1));
//            userViewMock.verify(() -> UserView.askTransactionIncome(), times(1));
//            userViewMock.verify(() -> UserView.askDescription(), times(1));
//            userViewMock.verify(() -> UserView.askAmount(), times(1));
//            transactionServiceMock.verify(() -> TransactionService.newTransaction(
//                            eq(mockUser), eq(true), any(BigDecimal.class), eq(Category.INCOME), any(Date.class), eq("Salary")),
//                    times(1));
//            userViewMock.verify(() -> UserView.printSuccess(), times(1));
//            userMenuControllerMock.verify(() -> UserMenuController.showUserMenu(mockUser), times(1));
//        }
//    }

    @Test
    void testUserMenuBudget_ViewBudget() {
        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
             MockedStatic<BudgetService> budgetServiceMock = mockStatic(BudgetService.class)) {

            // Arrange
            BigDecimal budget = new BigDecimal("5000.00");
            when(ConsoleView.readInt())
                    .thenReturn(1)  // View budget
                    .thenReturn(0); // Back
            when(BudgetService.getBudget(mockUser)).thenReturn(budget);

            // Act
            UserMenuController.userMenuBudget(mockUser);

            // Assert
            userViewMock.verify(() -> UserView.printBudgetMenu(mockUser), times(1));
            budgetServiceMock.verify(() -> BudgetService.getBudget(mockUser), times(1));
            userViewMock.verify(() -> UserView.printCurrentBudget(budget), times(1));
        }
    }

    @Test
    void testUserMenuGoal_SetGoal() {
        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class);
             MockedStatic<GoalService> goalServiceMock = mockStatic(GoalService.class)) {

            // Arrange
            BigDecimal newGoal = new BigDecimal("10000.00");
            when(ConsoleView.readInt())
                    .thenReturn(2)  // Set goal
                    .thenReturn(0); // Back
            when(ConsoleView.readBigDecimal()).thenReturn(newGoal);

            // Act
            UserMenuController.userMenuGoal(mockUser);

            // Assert
            userViewMock.verify(() -> UserView.printGoalMenu(mockUser), times(1));
            userViewMock.verify(() -> UserView.askGoal(), times(1));
            goalServiceMock.verify(() -> GoalService.setGoal(mockUser, newGoal), times(1));
        }
    }

    @Test
    void testUserMenuStats_ViewBalance() {
        try (MockedStatic<UserView> userViewMock = mockStatic(UserView.class);
             MockedStatic<ConsoleView> consoleViewMock = mockStatic(ConsoleView.class)) {

            // Arrange
            when(ConsoleView.readInt())
                    .thenReturn(1)  // View balance
                    .thenReturn(0); // Back

            // Act
            UserMenuController.userMenuStats(mockUser);

            // Assert
            userViewMock.verify(() -> UserView.askStats(), times(1));
            userViewMock.verify(() -> UserView.printUserBalance(mockUser), times(1));
        }
    }
}

