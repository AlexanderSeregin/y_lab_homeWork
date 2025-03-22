package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

class TransactionTest {

    private Transaction transaction;
    private final long USER_ID = 123L;
    private final boolean IS_INCOME = true;
    private final String DESCRIPTION = "Salary";
    private final BigDecimal AMOUNT = new BigDecimal("1000.00");
    private final Category CATEGORY = Category.INCOME;
    private final Date DATE = new Date();

    @BeforeEach
    void setUp() {
        transaction = new Transaction(USER_ID, IS_INCOME, DESCRIPTION, AMOUNT, CATEGORY, DATE);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(USER_ID, transaction.getUserId());
        assertEquals(AMOUNT, transaction.getAmount());
        assertEquals(CATEGORY, transaction.getCategory());
        assertEquals(DATE, transaction.getDate());
        assertTrue(transaction.getId() >= 0);
    }

    @Test
    void testUniqueIds() {
        Transaction transaction1 = new Transaction(1L, true, "Test1", new BigDecimal("100"), Category.INCOME, new Date());
        Transaction transaction2 = new Transaction(2L, false, "Test2", new BigDecimal("200"), Category.GROCERIES, new Date());

        assertNotEquals(transaction1.getId(), transaction2.getId());
    }

    @Test
    void testSetDescription() {
        String newDescription = "Updated description";
        transaction.setDescription(newDescription);

        // Use toString to check the description since getDescription is private
        String result = transaction.toString(transaction);
        assertTrue(result.contains("description='" + newDescription + "'"));
    }

    @Test
    void testSetAmount() {
        BigDecimal newAmount = new BigDecimal("2000.00");
        transaction.setAmount(newAmount);
        assertEquals(newAmount, transaction.getAmount());
    }

    @Test
    void testSetCategory() {
        Category newCategory = Category.ENTERTAINMENT;
        transaction.setCategory(newCategory);
        assertEquals(newCategory, transaction.getCategory());
    }

    @Test
    void testToString() {
        String result = transaction.toString(transaction);

        assertTrue(result.contains("id=" + transaction.getId()));
        assertTrue(result.contains("userId=" + USER_ID));
        assertTrue(result.contains("amount=" + AMOUNT));
        assertTrue(result.contains("category=" + CATEGORY));
        assertTrue(result.contains("date=" + DATE));
    }

    @Test
    void testWithZeroAndNegativeValues() {
        // Test with zero amount
        Transaction zeroTransaction = new Transaction(USER_ID, IS_INCOME, DESCRIPTION, BigDecimal.ZERO, CATEGORY, DATE);
        assertEquals(BigDecimal.ZERO, zeroTransaction.getAmount());

        // Test with negative amount
        BigDecimal negativeAmount = new BigDecimal("-500.00");
        Transaction negativeTransaction = new Transaction(USER_ID, IS_INCOME, DESCRIPTION, negativeAmount, CATEGORY, DATE);
        assertEquals(negativeAmount, negativeTransaction.getAmount());
    }
}