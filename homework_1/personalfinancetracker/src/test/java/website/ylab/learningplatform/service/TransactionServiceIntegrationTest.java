package website.ylab.learningplatform.service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresTransactionRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class TransactionServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("finance_tracker")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))))
            ;

    private User testUser;
    private final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private final BigDecimal TRANSACTION_AMOUNT = new BigDecimal("100.00");

    @BeforeAll
    static void startContainer() {
        postgres.start();

        // Configure database connection properties
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USER", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());

        LiquibaseConfig.getInstance().migrate();
    }

    @BeforeEach
    void setUp() {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
             Statement stmt = conn.createStatement()) {
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set up test data", e);
        }

        // Initialize test user object
        testUser = new User();
        testUser.setId(2L);
        testUser.setEmail("user@user.com");
        testUser.setName("user");
        testUser.setPassword("cGVyc29uYWxGaW5hbmNlQXBwdXNlcg==");
        testUser.setBalance(INITIAL_BALANCE);
    }


    @Test
    void newTransaction_Expense_ShouldDecreaseBalance() {
        // Act
        TransactionService.newTransaction(testUser, false, TRANSACTION_AMOUNT, Category.GROCERIES, new Date(), "Test expense");

        // Assert
        Optional<User> updatedUser = PostgresUserRepository.getInstance().findById(testUser.getId());
        assertTrue(updatedUser.isPresent(), "User should exist after transaction");
        assertEquals(0, INITIAL_BALANCE.subtract(TRANSACTION_AMOUNT).compareTo(updatedUser.get().getBalance()),
                "Balance should decrease by transaction amount");

        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(testUser.getId());
        assertTrue(transactions.isPresent(), "Transactions should exist");
        assertFalse(transactions.get().isEmpty(), "Transaction list should not be empty");
        
        Transaction transaction = transactions.get().get(0);
        assertEquals(0, TRANSACTION_AMOUNT.negate().compareTo(transaction.getAmount()),
                "Transaction amount should be negative for expense");
        assertFalse(transaction.isIncome(), "Transaction should be marked as expense");
        assertEquals(Category.GROCERIES, transaction.getCategory());
    }



    @Test
    void getSumOfUserSpendingsByCategoryForCurrentMonth_WithMultipleCategories_ShouldReturnCorrectMap() {
        // Arrange
        TransactionService.newTransaction(testUser, false, TRANSACTION_AMOUNT, Category.GROCERIES, new Date(), "Groceries expense");
        TransactionService.newTransaction(testUser, false, TRANSACTION_AMOUNT, Category.TRANSPORTATION, new Date(), "Transport expense");

        // Act
        Map<Category, BigDecimal> spendingsByCategory = TransactionService.getSumOfUserSpendingsByCategoryForCurrentMonth(testUser);

        // Assert
        assertNotNull(spendingsByCategory, "Spendings map should not be null");
        assertEquals(2, spendingsByCategory.size(), "Should have two categories");
        assertTrue(spendingsByCategory.containsKey(Category.GROCERIES), "Should contain groceries category");
        assertTrue(spendingsByCategory.containsKey(Category.TRANSPORTATION), "Should contain transportation category");
        assertEquals(0, TRANSACTION_AMOUNT.compareTo(spendingsByCategory.get(Category.GROCERIES).abs()),
                "Groceries spending should match");
        assertEquals(0, TRANSACTION_AMOUNT.compareTo(spendingsByCategory.get(Category.TRANSPORTATION).abs()),
                "Transportation spending should match");
    }
}
