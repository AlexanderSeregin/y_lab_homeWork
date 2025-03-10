package website.ylab.learningplatform.service.interfaces;

import website.ylab.learningplatform.model.Goal;

import java.math.BigDecimal;

/**
 * Service interface for goal management operations
 */
public interface GoalService {
    /**
     * Get goal for user
     * @param userId user id
     * @return user goal or null if not found
     */
    Goal getGoal(Long userId);
    
    /**
     * Set goal for user
     * @param userId user id
     * @param amount goal amount
     * @return updated goal
     */
    Goal setGoal(Long userId, BigDecimal amount);
}
