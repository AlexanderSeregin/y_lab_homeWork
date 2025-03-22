package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class Goal {
    private Long id;
    private Long userId;
    private BigDecimal amount;

    public Goal() {
    }

    public Goal(BigDecimal newGoal) {
        this.amount = newGoal;
    }

    public Goal(Long id, Long userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long l) {
        this.userId = l;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long goalId) {
        this.id = goalId;
    }
}
