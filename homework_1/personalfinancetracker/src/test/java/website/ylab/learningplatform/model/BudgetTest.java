package website.ylab.learningplatform.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BudgetTest {

    private Budget budget;
    private final Long userId = 1L;
    private final BigDecimal amount = BigDecimal.valueOf(1000);

    @BeforeEach
    void setUp() {
        budget = new Budget(userId, amount);
    }

    @Test
    void constructor_ShouldSetFieldsCorrectly() {
        assertEquals(userId, budget.getUserId());
        assertEquals(amount, budget.getAmount());
    }

    @Test
    void setUserId_ShouldUpdateUserId() {
        long newUserId = 2L;
        budget.setUserId(newUserId);
        assertEquals(newUserId, budget.getUserId());
    }

    @Test
    void setAmount_ShouldUpdateAmount() {
        BigDecimal newAmount = BigDecimal.valueOf(2000);
        budget.setAmount(newAmount);
        assertEquals(newAmount, budget.getAmount());
    }

    @Test
    void getAmount_ShouldReturnCorrectAmount() {
        assertEquals(amount, budget.getAmount());
    }

    @Test
    void getUserId_ShouldReturnCorrectUserId() {
        assertEquals(userId, budget.getUserId());
    }
}
