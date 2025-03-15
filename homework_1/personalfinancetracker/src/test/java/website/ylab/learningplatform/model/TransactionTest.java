package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void constructor_WithAllParameters_ShouldSetCorrectly() {
        Date date = new Date();
        Transaction transaction = new Transaction(1L, 1L, true, "Test transaction", BigDecimal.valueOf(100), Category.GROCERIES, date);
        
        assertEquals(1L, transaction.getId());
        assertEquals(1L, transaction.getUserId());
        assertTrue(transaction.isIncome());
        assertEquals("Test transaction", transaction.getDescription());
        assertEquals(BigDecimal.valueOf(100), transaction.getAmount());
        assertEquals(Category.GROCERIES, transaction.getCategory());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void constructor_WithNullDate_ShouldUseCurrentDate() {
        Transaction transaction = new Transaction(1L, true, "Test transaction", BigDecimal.valueOf(100), Category.GROCERIES, null);
        
        assertNotNull(transaction.getDate());
    }

    @Test
    void createIncome_WithPositiveAmount_ShouldCreateTransaction() {
        Date date = new Date();
        Transaction transaction = Transaction.createIncome(1L, "Test income", BigDecimal.valueOf(100), Category.GROCERIES, date);
        
        assertTrue(transaction.isIncome());
        assertEquals(BigDecimal.valueOf(100), transaction.getAmount());
    }

    @Test
    void createIncome_WithZeroAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            Transaction.createIncome(1L, "Test income", BigDecimal.ZERO, Category.GROCERIES, new Date())
        );
    }

    @Test
    void createIncome_WithNegativeAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            Transaction.createIncome(1L, "Test income", BigDecimal.valueOf(-100), Category.GROCERIES, new Date())
        );
    }

    @Test
    void createExpense_WithPositiveAmount_ShouldCreateTransactionWithNegativeAmount() {
        Date date = new Date();
        Transaction transaction = Transaction.createExpense(1L, "Test expense", BigDecimal.valueOf(100), Category.GROCERIES, date);
        
        assertFalse(transaction.isIncome());
        assertEquals(BigDecimal.valueOf(-100), transaction.getAmount());
    }

    @Test
    void createExpense_WithZeroAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            Transaction.createExpense(1L, "Test expense", BigDecimal.ZERO, Category.GROCERIES, new Date())
        );
    }

    @Test
    void createExpense_WithNegativeAmount_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
            Transaction.createExpense(1L, "Test expense", BigDecimal.valueOf(-100), Category.GROCERIES, new Date())
        );
    }

    @Test
    void getAbsoluteAmount_WithPositiveAmount_ShouldReturnSameAmount() {
        Transaction transaction = new Transaction(1L, true, "Test", BigDecimal.valueOf(100), Category.GROCERIES, new Date());
        assertEquals(BigDecimal.valueOf(100), transaction.getAbsoluteAmount());
    }

    @Test
    void getAbsoluteAmount_WithNegativeAmount_ShouldReturnPositiveAmount() {
        Transaction transaction = new Transaction(1L, false, "Test", BigDecimal.valueOf(-100), Category.GROCERIES, new Date());
        assertEquals(BigDecimal.valueOf(100), transaction.getAbsoluteAmount());
    }

    @Test
    void setDescription_ShouldUpdateDescription() {
        Transaction transaction = new Transaction(1L, true, "Original", BigDecimal.valueOf(100), Category.GROCERIES, new Date());
        transaction.setDescription("Updated");
        assertEquals("Updated", transaction.getDescription());
    }

    @Test
    void setCategory_ShouldUpdateCategory() {
        Transaction transaction = new Transaction(1L, true, "Test", BigDecimal.valueOf(100), Category.GROCERIES, new Date());
        transaction.setCategory(Category.ENTERTAINMENT);
        assertEquals(Category.ENTERTAINMENT, transaction.getCategory());
    }

    @Test
    void setAmount_ShouldUpdateAmount() {
        Transaction transaction = new Transaction(1L, true, "Test", BigDecimal.valueOf(100), Category.GROCERIES, new Date());
        transaction.setAmount(BigDecimal.valueOf(200));
        assertEquals(BigDecimal.valueOf(200), transaction.getAmount());
    }

    @Test
    void toString_ShouldContainAllFields() {
        Date date = new Date();
        Transaction transaction = new Transaction(1L, 1L, true, "Test", BigDecimal.valueOf(100), Category.GROCERIES, date);
        
        String result = transaction.toString();
        
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("userId=1"));
        assertTrue(result.contains("isIncome=true"));
        assertTrue(result.contains("description='Test'"));
        assertTrue(result.contains("amount=100"));
        assertTrue(result.contains("category=GROCERIES"));
        assertTrue(result.contains("date=" + date.toString()));
    }
}
