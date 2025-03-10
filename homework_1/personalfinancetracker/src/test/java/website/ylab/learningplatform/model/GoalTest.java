package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class GoalTest {

    @Test
    public void testConstructor() {
        // Given
        BigDecimal expectedAmount = new BigDecimal("100.50");

        // When
        Goal goal = new Goal(expectedAmount);

        // Then
        assertNotNull(goal, "Goal instance should not be null");
        assertEquals(expectedAmount, goal.getAmount(), "Amount should match the constructor parameter");
    }

    @Test
    public void testGetAmount() {
        // Given
        BigDecimal amount = new BigDecimal("1000");
        Goal goal = new Goal(amount);

        // When
        BigDecimal retrievedAmount = goal.getAmount();

        // Then
        assertEquals(amount, retrievedAmount, "getAmount() should return the amount set in constructor");
        assertSame(amount, retrievedAmount, "getAmount() should return the same BigDecimal instance");
    }

    @Test
    public void testZeroAmount() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // When
        Goal goal = new Goal(zeroAmount);

        // Then
        assertEquals(BigDecimal.ZERO, goal.getAmount(), "Should handle zero amount correctly");
    }

    @Test
    public void testNegativeAmount() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-50.25");

        // When
        Goal goal = new Goal(negativeAmount);

        // Then
        assertEquals(negativeAmount, goal.getAmount(), "Should handle negative amount correctly");
    }

    @Test
    public void testNullAmount() {
        // Given/When/Then
        assertDoesNotThrow(() -> new Goal(null), "Should handle null amount without throwing exception");

        Goal goal = new Goal(null);
        assertNull(goal.getAmount(), "getAmount() should return null when constructed with null");
    }
}