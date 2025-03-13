package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Goal {
    private Long userId;
    private final BigDecimal amount;

    public Goal(BigDecimal newGoal) {
        this.amount = newGoal;
    }

    public BigDecimal getAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
    }
        return amount;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Long l) {
        this.userId = l;
    }
}
