package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Goal;

import java.util.HashMap;
import java.util.Optional;

public class GoalDao {
    private static final GoalDao INSTANCE = new GoalDao();

    /**
     * Gets the single instance of GoalDao.
     * @return The GoalDao instance.
     */
    public static GoalDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, Goal> repository = new HashMap<>();

    /**
     * Save a goal for a user.
     * @param userId The user who is the target of the goal.
     * @param goal The goal to save.
     */
    public void save(long userId, Goal goal) {
        repository.put(userId, goal);
    }

    /**
     * Retrieve a goal for a user.
     * @param id The user to retrieve a goal for.
     * @return An optional containing the goal if it exists, otherwise an empty optional.
     */
    public Optional<Goal> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }
}
