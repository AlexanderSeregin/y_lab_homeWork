package website.ylab.learningplatform.repository;

import website.ylab.learningplatform.model.Budget;

import java.util.Optional;

/**
 * Repository interface for Budget entity
 */
public interface BudgetRepository extends Repository<Budget, Long> {
    /**
     * Find budget by user ID
     *
     * @param userId user ID
     * @return Optional containing budget if found
     */
    Optional<Budget> findByUserId(Long userId);
}
