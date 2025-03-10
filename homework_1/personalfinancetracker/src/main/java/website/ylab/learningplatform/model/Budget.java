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
