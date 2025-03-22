package website.ylab.learningplatform.repository;

import website.ylab.learningplatform.model.Goal;

import java.util.Optional;

/**
 * Repository interface for Goal entity
 */
public interface GoalRepository extends Repository<Goal, Long> {
    /**
     * Find goal by user ID
     *
     * @param userId user ID
     * @return Optional containing goal if found
     */
    Optional<Goal> findByUserId(Long userId);
}
