package website.ylab.learningplatform.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import website.ylab.learningplatform.config.DatabaseConfig;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.repository.GoalRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of GoalRepository
 */
@Repository
public class PostgresGoalRepository extends PostgresRepository<Goal, Long> implements GoalRepository {

    private static final String SELECT_BY_ID = "SELECT * FROM finance_schema.goals WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM finance_schema.goals";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM finance_schema.goals WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO finance_schema.goals (id, user_id, target_amount) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE finance_schema.goals SET target_amount = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM finance_schema.goals WHERE id = ?";

    @Autowired
    public PostgresGoalRepository(DatabaseConfig dbConfig) {
        super(dbConfig);
    }

    /**
     * Get singleton instance
     *
     * @return repository instance
     */

    @Override
    public Optional<Goal> findById(Long id) {
        return querySingle(SELECT_BY_ID, this::mapResultSetToGoal, id);
    }

    @Override
    public Goal save(Goal goal) {
        if (goal.getId() == null) {
            Long goalId = getNextSequenceValue("service_schema.goal_seq");
            goal.setId(goalId);
            executeUpdate(INSERT,
                    goal.getId(),
                    goal.getUserId(),
                    goal.getAmount());
        } else {
            executeUpdate(UPDATE,
                    goal.getAmount(),
                    goal.getId());
        }
        return goal;
    }

    @Override
    public List<Goal> findAll() {
        return queryList(SELECT_ALL, this::mapResultSetToGoal);
    }

    @Override
    public void delete(Goal goal) {
        if (goal.getId() != null) {
            deleteById(goal.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        executeUpdate(DELETE, id);
    }

    @Override
    public Optional<Goal> findByUserId(Long userId) {
        return querySingle(SELECT_BY_USER_ID, this::mapResultSetToGoal, userId);
    }

    /**
     * Map database result to Goal entity
     *
     * @param rs ResultSet containing goal data
     * @return mapped Goal entity
     * @throws SQLException if mapping fails
     */
    private Goal mapResultSetToGoal(ResultSet rs) throws SQLException {
        Long userId = rs.getLong("user_id");
        Goal goal = new Goal(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBigDecimal("target_amount")
        );
        return goal;
    }
}
