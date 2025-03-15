package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {

    @Test
    void constructor_WithAmount_ShouldSetAmount() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        Goal goal = new Goal(amount);
        
        assertEquals(amount, goal.getAmount());
    }

    @Test
    void constructor_WithAllParameters_ShouldSetAllFields() {
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = BigDecimal.valueOf(1000);
        
        Goal goal = new Goal(id, userId, amount);
        
        assertEquals(id, goal.getId());
        assertEquals(userId, goal.getUserId());
        assertEquals(amount, goal.getAmount());
    }

    @Test
    void getAmount_WhenAmountIsNull_ShouldReturnZero() {
        Goal goal = new Goal(null);
        assertEquals(BigDecimal.ZERO, goal.getAmount());
    }

    @Test
    void setAmount_ShouldUpdateAmount() {
        Goal goal = new Goal(BigDecimal.valueOf(1000));
        BigDecimal newAmount = BigDecimal.valueOf(2000);
        
        goal.setAmount(newAmount);
        
        assertEquals(newAmount, goal.getAmount());
    }

    @Test
    void setUserId_ShouldUpdateUserId() {
        Goal goal = new Goal(1L, 1L, BigDecimal.valueOf(1000));
        Long newUserId = 2L;
        
        goal.setUserId(newUserId);
        
        assertEquals(newUserId, goal.getUserId());
    }

    @Test
    void setId_ShouldUpdateId() {
        Goal goal = new Goal(1L, 1L, BigDecimal.valueOf(1000));
        Long newId = 2L;
        
        goal.setId(newId);
        
        assertEquals(newId, goal.getId());
    }

    @Test
    void getUserId_WhenNotSet_ShouldReturnNull() {
        Goal goal = new Goal(BigDecimal.valueOf(1000));
        assertNull(goal.getUserId());
    }

    @Test
    void getId_WhenNotSet_ShouldReturnNull() {
        Goal goal = new Goal(BigDecimal.valueOf(1000));
        assertNull(goal.getId());
    }
}
