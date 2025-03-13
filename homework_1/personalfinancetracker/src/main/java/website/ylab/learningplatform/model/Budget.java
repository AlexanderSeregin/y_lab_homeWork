package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Budget {
    private long userId;
    private BigDecimal amount;

    public Budget(Long userId, BigDecimal budget) {
        this.userId = userId;
        this.amount = budget;
    }

    /**
     * Gets the user ID associated with this budget.
     * 
     * @return the user ID
     */
    public long getUserId() {
        return userId;
    }
    
    /**
     * Sets the user ID associated with this budget.
     * 
     * @param userId the user ID to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Gets the budget amount for the user.
     *
     * @return the budget amount in the smallest unit of currency
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
