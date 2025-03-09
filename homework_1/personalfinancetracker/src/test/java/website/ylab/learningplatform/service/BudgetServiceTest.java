package website.ylab.learningplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.datasource.BudgetDao;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    private User testUser;
    private BudgetDao mockBudgetDao;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "password");
        testUser.setId(1L);

        mockBudgetDao = mock(BudgetDao.class);
    }

    @Test
    void testGetBudget_WhenBudgetExists() {
        // Setup
        BigDecimal expectedBudget = new BigDecimal("1000.00");
        Budget budget = new Budget(expectedBudget);

        when(mockBudgetDao.get(testUser.getId())).thenReturn(Optional.of(budget));

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            // Execute
            BigDecimal result = BudgetService.getBudget(testUser);

            // Verify
            assertEquals(expectedBudget, result);
            verify(mockBudgetDao).get(testUser.getId());
        }
    }

    @Test
    void testGetBudget_WhenBudgetDoesNotExist() {
        // Setup
        when(mockBudgetDao.get(testUser.getId())).thenReturn(Optional.empty());

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            // Execute
            BigDecimal result = BudgetService.getBudget(testUser);

            // Verify
            assertEquals(BigDecimal.ZERO, result);
            verify(mockBudgetDao).get(testUser.getId());
        }
    }

    @Test
    void testSetBudget() {
        // Setup
        BigDecimal newBudget = new BigDecimal("2000.00");

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            // Execute
            BudgetService.setBudget(testUser, newBudget);

            // Verify
            verify(mockBudgetDao).save(eq(testUser.getId()), any(Budget.class));
        }
    }

    @Test
    void testCheckBudget_WhenBudgetExistsAndExceeded() {
        // Setup
        BigDecimal budget = new BigDecimal("1000.00");
        BigDecimal spentAmount = new BigDecimal("1200.00");
        Budget budgetObj = new Budget(budget);

        when(mockBudgetDao.get(testUser.getId())).thenReturn(Optional.of(budgetObj));

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            try (MockedStatic<TransactionService> mockedTransactionService = Mockito.mockStatic(TransactionService.class)) {
                mockedTransactionService.when(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser))
                        .thenReturn(spentAmount);

                try (MockedStatic<NotificationService> mockedNotificationService = Mockito.mockStatic(NotificationService.class)) {
                    // Execute
                    BudgetService.checkBudget(testUser);

                    // Verify
                    verify(mockBudgetDao).get(testUser.getId());
                    mockedTransactionService.verify(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser));
                    mockedNotificationService.verify(() ->
                            NotificationService.sendNotification(eq(testUser), eq("You have exceeded your budget for the month!")));
                }
            }
        }
    }

    @Test
    void testCheckBudget_WhenBudgetExistsButNotExceeded() {
        // Setup
        BigDecimal budget = new BigDecimal("1000.00");
        BigDecimal spentAmount = new BigDecimal("800.00");
        Budget budgetObj = new Budget(budget);

        when(mockBudgetDao.get(testUser.getId())).thenReturn(Optional.of(budgetObj));

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            try (MockedStatic<TransactionService> mockedTransactionService = Mockito.mockStatic(TransactionService.class)) {
                mockedTransactionService.when(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser))
                        .thenReturn(spentAmount);

                try (MockedStatic<NotificationService> mockedNotificationService = Mockito.mockStatic(NotificationService.class)) {
                    // Execute
                    BudgetService.checkBudget(testUser);

                    // Verify
                    verify(mockBudgetDao).get(testUser.getId());
                    mockedTransactionService.verify(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser));
                    mockedNotificationService.verifyNoInteractions();
                }
            }
        }
    }

    @Test
    void testCheckBudget_WhenBudgetDoesNotExist() {
        // Setup
        when(mockBudgetDao.get(testUser.getId())).thenReturn(Optional.empty());

        try (MockedStatic<BudgetDao> mockedStatic = Mockito.mockStatic(BudgetDao.class)) {
            mockedStatic.when(BudgetDao::getInstance).thenReturn(mockBudgetDao);

            try (MockedStatic<TransactionService> mockedTransactionService = Mockito.mockStatic(TransactionService.class)) {
                mockedTransactionService.when(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser))
                        .thenReturn(BigDecimal.valueOf(500));

                // Execute
                BudgetService.checkBudget(testUser);

                // Verify
                verify(mockBudgetDao).get(testUser.getId());
                // Since budget doesn't exist, we shouldn't proceed to check transactions or send notifications
                mockedTransactionService.verify(() -> TransactionService.getSumOfUserSpendingsInCurrentMonth(testUser));
            }
        }
    }
}