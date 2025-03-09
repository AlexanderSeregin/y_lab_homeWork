package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Budget {
    private BigDecimal amount;

    public Budget(BigDecimal budget) {
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

}
