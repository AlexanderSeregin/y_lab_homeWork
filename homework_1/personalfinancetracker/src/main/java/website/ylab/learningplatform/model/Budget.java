package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Budget {
    private BigDecimal amount;

    public Budget(BigDecimal budget) {
        this.amount = budget;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
