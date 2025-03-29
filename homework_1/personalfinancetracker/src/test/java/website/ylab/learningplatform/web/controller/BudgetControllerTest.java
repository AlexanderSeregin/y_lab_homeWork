package website.ylab.learningplatform.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.UserService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BudgetController budgetController;

    private User testUser;
    private Budget testBudget;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setBalance(new BigDecimal("1000.00"));
        testUser.setAdmin(false);

        // Setup test budget
        testBudget = new Budget();
        testBudget.setId(1L);
        testBudget.setUserId(1L);
        testBudget.setAmount(new BigDecimal("500.00"));
    }

    @Test
    void getUserBudget_UserExists_ReturnsBudget() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(budgetService.getUserBudget(1L)).thenReturn(testBudget);

        // Act
        ResponseEntity<?> response = budgetController.getUserBudget(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Budget);
        Budget returnedBudget = (Budget) response.getBody();
        assertEquals(testBudget.getId(), returnedBudget.getId());
        assertEquals(testBudget.getAmount(), returnedBudget.getAmount());
    }

    @Test
    void getUserBudget_UserExistsNoBudget_ReturnsZero() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(budgetService.getUserBudget(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = budgetController.getUserBudget(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof BigDecimal);
        assertEquals(BigDecimal.ZERO, response.getBody());
    }

    @Test
    void getUserBudget_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = budgetController.getUserBudget(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(budgetService, never()).getUserBudget(anyLong());
    }

    @Test
    void setBudget_ValidAmount_ReturnsBudget() {
        // Arrange
        BigDecimal amount = new BigDecimal("750.00");
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(budgetService.setBudget(1L, amount)).thenReturn(testBudget);

        // Act
        ResponseEntity<?> response = budgetController.setBudget(amount, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Budget);
        verify(budgetService).setBudget(1L, amount);
    }

    @Test
    void setBudget_NegativeAmount_ReturnsBadRequest() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = budgetController.setBudget(negativeAmount, 1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Budget amount cannot be negative", response.getBody());
        verify(budgetService, never()).setBudget(anyLong(), any());
    }

    @Test
    void setBudget_UserNotFound_ReturnsNotFound() {
        // Arrange
        BigDecimal amount = new BigDecimal("500.00");
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = budgetController.setBudget(amount, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(budgetService, never()).setBudget(anyLong(), any());
    }

    @Test
    void updateBudget_ValidBudget_ReturnsUpdatedBudget() {
        // Arrange
        Long budgetId = 1L;
        BigDecimal newAmount = new BigDecimal("800.00");
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(budgetService.getUserBudget(1L)).thenReturn(testBudget);
        
        Budget updatedBudget = new Budget();
        updatedBudget.setId(1L);
        updatedBudget.setUserId(1L);
        updatedBudget.setAmount(newAmount);
        when(budgetService.updateBudget(any(Budget.class))).thenReturn(updatedBudget);

        // Act
        ResponseEntity<?> response = budgetController.updateBudget(budgetId, newAmount, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Budget);
        Budget returnedBudget = (Budget) response.getBody();
        assertEquals(newAmount, returnedBudget.getAmount());
        verify(budgetService).updateBudget(any(Budget.class));
    }

    @Test
    void updateBudget_NegativeAmount_ReturnsBadRequest() {
        // Arrange
        Long budgetId = 1L;
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = budgetController.updateBudget(budgetId, negativeAmount, 1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Budget amount cannot be negative", response.getBody());
        verify(budgetService, never()).updateBudget(any(Budget.class));
    }

    @Test
    void updateBudget_NoBudgetFound_ReturnsForbidden() {
        // Arrange
        Long budgetId = 1L;
        BigDecimal newAmount = new BigDecimal("800.00");
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(budgetService.getUserBudget(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = budgetController.updateBudget(budgetId, newAmount, 1L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not authorized to update this budget", response.getBody());
        verify(budgetService, never()).updateBudget(any(Budget.class));
    }

    @Test
    void updateBudget_UserNotFound_ReturnsNotFound() {
        // Arrange
        Long budgetId = 1L;
        BigDecimal newAmount = new BigDecimal("800.00");
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = budgetController.updateBudget(budgetId, newAmount, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(budgetService, never()).getUserBudget(anyLong());
        verify(budgetService, never()).updateBudget(any(Budget.class));
    }
}
