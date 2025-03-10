package website.ylab.learningplatform.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDaoTest {

    private TransactionDao transactionDao;
    private User testUser;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        transactionDao = TransactionDao.getInstance();
        // Clear any existing data by using reflection to reset the repository
        try {
            java.lang.reflect.Field field = TransactionDao.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(transactionDao, new java.util.HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        testUser = new User("Test User", "test@example.com", "hashedPassword");
        testTransaction = new Transaction(testUser.getId(), true, "Test Income",
                new BigDecimal("100.00"), Category.INCOME, new Date());
    }

    @Test
    void testSaveAndGet() {
        transactionDao.save(testTransaction);

        Optional<List<Transaction>> result = transactionDao.get(testUser.getId());

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(testTransaction, result.get().get(0));
    }

    @Test
    void testGetTransactionsByUser() {
        transactionDao.save(testTransaction);

        Transaction anotherTransaction = new Transaction(testUser.getId(), false, "Test Expense",
                new BigDecimal("50.00"), Category.GROCERIES, new Date());
        transactionDao.save(anotherTransaction);

        List<Transaction> transactions = transactionDao.getTransactionsByUser(testUser);

        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(testTransaction));
        assertTrue(transactions.contains(anotherTransaction));
    }

    @Test
    void testGetAll() {
        transactionDao.save(testTransaction);

        User anotherUser = new User("Another User", "another@example.com", "anotherPassword");
        Transaction anotherTransaction = new Transaction(anotherUser.getId(), false, "Another Expense",
                new BigDecimal("25.00"), Category.ENTERTAINMENT, new Date());
        transactionDao.save(anotherTransaction);

        Iterable<Transaction> transactions = transactionDao.getAll();
        List<Transaction> transactionList = new java.util.ArrayList<>();
        transactions.forEach(transactionList::add);

        assertEquals(2, transactionList.size());
        assertTrue(transactionList.contains(testTransaction));
        assertTrue(transactionList.contains(anotherTransaction));
    }

    @Test
    void testGet_NotFound() {
        Optional<List<Transaction>> result = transactionDao.get(999L);

        assertFalse(result.isPresent());
    }
}