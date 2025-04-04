package website.ylab.learningplatform.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.TransactionService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.TransactionDto;
import website.ylab.learningplatform.web.mapper.TransactionMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionController transactionController;

    private User testUser;
    private User adminUser;
    private Transaction testTransaction;
    private TransactionDto testTransactionDto;
    private Date testDate;

    @BeforeEach
    void setUp() {
        // Setup test date
        testDate = new Date();

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setBalance(new BigDecimal("1000.00"));
        testUser.setAdmin(false);

        // Setup admin user
        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setEmail("admin@example.com");
        adminUser.setBalance(new BigDecimal("2000.00"));
        adminUser.setAdmin(true);

        // Setup test transaction
        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setUserId(1L);
        testTransaction.setIncome(true);
        testTransaction.setDescription("Test Transaction");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setCategory(Category.INCOME);
        testTransaction.setDate(testDate);

        // Setup transaction DTO
        testTransactionDto = new TransactionDto(
                1L, 1L, true, "Test Transaction",
                new BigDecimal("100.00"), Category.INCOME, testDate);
    }

    @Test
    void getAllTransactions_UserExists_ReturnsTransactions() {
        // Arrange
        List<Transaction> transactions = Collections.singletonList(testTransaction);
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionService.getUserTransactions(1L)).thenReturn(transactions);
        when(transactionMapper.toDto(testTransaction)).thenReturn(testTransactionDto);

        // Act
        ResponseEntity<?> response = transactionController.getAllTransactions(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        List<?> returnedTransactions = (List<?>) response.getBody();
        assertEquals(1, returnedTransactions.size());
        assertEquals(testTransactionDto, returnedTransactions.get(0));
    }

    @Test
    void getAllTransactions_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.getAllTransactions(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, never()).getUserTransactions(any());
    }

    @Test
    void createTransaction_ValidTransaction_ReturnsCreatedTransaction() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionMapper.toEntity(testTransactionDto)).thenReturn(testTransaction);
        when(transactionService.createTransaction(testTransaction)).thenReturn(testTransaction);
        when(transactionMapper.toDto(testTransaction)).thenReturn(testTransactionDto);

        // Act
        ResponseEntity<?> response = transactionController.createTransaction(testTransactionDto, 1L);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testTransactionDto, response.getBody());
        verify(transactionService).createTransaction(testTransaction);
    }

    @Test
    void createTransaction_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.createTransaction(testTransactionDto, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, never()).createTransaction(any());
    }

    @Test
    void createTransaction_ServiceException_ReturnsInternalServerError() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionMapper.toEntity(testTransactionDto)).thenReturn(testTransaction);
        when(transactionService.createTransaction(testTransaction)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = transactionController.createTransaction(testTransactionDto, 1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertTrue(errorMap.get("error").contains("Error creating transaction"));
    }

    @Test
    void getTransaction_ValidIdAndOwner_ReturnsTransaction() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);
        when(transactionMapper.toDto(testTransaction)).thenReturn(testTransactionDto);

        // Act
        ResponseEntity<?> response = transactionController.getTransaction(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTransactionDto, response.getBody());
    }

    @Test
    void getTransaction_ValidIdAndAdmin_ReturnsTransaction() {
        // Arrange
        testTransaction.setUserId(1L); // Transaction belongs to user 1
        when(userService.getUserById(2L)).thenReturn(adminUser); // Admin user (id=2) is requesting
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);
        when(transactionMapper.toDto(testTransaction)).thenReturn(testTransactionDto);

        // Act
        ResponseEntity<?> response = transactionController.getTransaction(1L, 2L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTransactionDto, response.getBody());
    }

    @Test
    void getTransaction_ValidIdButNotOwnerOrAdmin_ReturnsForbidden() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(3L);
        otherUser.setAdmin(false);

        testTransaction.setUserId(1L); // Transaction belongs to user 1
        when(userService.getUserById(3L)).thenReturn(otherUser); // User 3 is requesting
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act
        ResponseEntity<?> response = transactionController.getTransaction(1L, 3L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(transactionMapper, never()).toDto(any());
    }

    @Test
    void getTransaction_TransactionNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionService.getTransactionById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.getTransaction(999L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionMapper, never()).toDto(any());
    }

    @Test
    void getTransaction_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.getTransaction(1L, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, never()).getTransactionById(any());
    }

    @Test
    void deleteTransaction_ValidIdAndOwner_ReturnsNoContent() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act
        ResponseEntity<?> response = transactionController.deleteTransaction(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService).deleteTransaction(testTransaction);
    }

    @Test
    void deleteTransaction_ValidIdAndAdmin_ReturnsNoContent() {
        // Arrange
        testTransaction.setUserId(1L); // Transaction belongs to user 1
        when(userService.getUserById(2L)).thenReturn(adminUser); // Admin user (id=2) is requesting
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act
        ResponseEntity<?> response = transactionController.deleteTransaction(1L, 2L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService).deleteTransaction(testTransaction);
    }

    @Test
    void deleteTransaction_ValidIdButNotOwnerOrAdmin_ReturnsForbidden() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(3L);
        otherUser.setAdmin(false);

        testTransaction.setUserId(1L); // Transaction belongs to user 1
        when(userService.getUserById(3L)).thenReturn(otherUser); // User 3 is requesting
        when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act
        ResponseEntity<?> response = transactionController.deleteTransaction(1L, 3L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(transactionService, never()).deleteTransaction(any());
    }

    @Test
    void deleteTransaction_TransactionNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(transactionService.getTransactionById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.deleteTransaction(999L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, never()).deleteTransaction(any());
    }

    @Test
    void deleteTransaction_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = transactionController.deleteTransaction(1L, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(transactionService, never()).getTransactionById(any());
        verify(transactionService, never()).deleteTransaction(any());
    }
}
