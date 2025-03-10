package website.ylab.learningplatform.service.interfaces;

import website.ylab.learningplatform.model.Budget;

import java.math.BigDecimal;

/**
 * Service interface for budget management operations
 */
public interface BudgetService {
    /**
     * Get budget for user
     * @param userId user id
     * @return user budget or null if not found
     */
    Budget getBudget(Long userId);
    
    /**
     * Set budget for user
     * @param userId user id
     * @param amount budget amount
     * @return updated budget
     */
    Budget setBudget(Long userId, BigDecimal amount);
    
    /**
     * Check if user exceeded budget
     * @param userId user id
     * @return true if budget exceeded
     */
    boolean isBudgetExceeded(Long userId);
}
