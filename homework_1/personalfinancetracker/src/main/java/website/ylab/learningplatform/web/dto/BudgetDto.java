package website.ylab.learningplatform.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class BudgetDto {
    private Long userId;
    @NotNull(message = "Limit amount is required")
    @Positive(message = "Limit amount must be positive")
    private BigDecimal amount;


    public BudgetDto() {
    }

    public BudgetDto(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getamount() {
        return amount;
    }

    public void setamount(BigDecimal limitAmount) {
        this.amount = limitAmount;
    }

}
