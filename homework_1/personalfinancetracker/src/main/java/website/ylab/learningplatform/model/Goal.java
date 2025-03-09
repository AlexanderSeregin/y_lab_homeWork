package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Goal {
    private BigDecimal amount;

    public Goal(BigDecimal newGoal) {
        this.amount = newGoal;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
