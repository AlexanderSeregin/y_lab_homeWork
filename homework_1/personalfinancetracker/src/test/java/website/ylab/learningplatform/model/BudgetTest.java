package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class BudgetTest {

    @Test
    public void testConstructor() {
        // Arrange
        BigDecimal expectedAmount = new BigDecimal("1000.00");

        // Act
        Budget budget = new Budget(expectedAmount);

        // Assert
        assertNotNull(budget, "Budget should not be null");
    }

    @Test
    public void testGetAmount() {
        // Arrange
        BigDecimal expectedAmount = new BigDecimal("1000.00");
        Budget budget = new Budget(expectedAmount);

        // Act
        BigDecimal actualAmount = budget.getAmount();

        // Assert
        assertEquals(expectedAmount, actualAmount, "getAmount should return the amount passed in constructor");
    }

    @Test
    public void testConstructorWithNegativeAmount() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-500.00");

        // Act
        Budget budget = new Budget(negativeAmount);

        // Assert
        assertEquals(negativeAmount, budget.getAmount(), "Budget should accept negative amounts");
    }

    @Test
    public void testConstructorWithZeroAmount() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act
        Budget budget = new Budget(zeroAmount);

        // Assert
        assertEquals(zeroAmount, budget.getAmount(), "Budget should accept zero as amount");
    }

    @Test
    public void testAmountImmutability() {
        // Arrange
        BigDecimal initialAmount = new BigDecimal("1000.00");
        Budget budget = new Budget(initialAmount);

        // Act
        BigDecimal returnedAmount = budget.getAmount();
        returnedAmount = returnedAmount.add(new BigDecimal("500.00"));

        // Assert
        assertEquals(initialAmount, budget.getAmount(),
                "Modifying the returned amount should not affect the original budget amount");
    }
}