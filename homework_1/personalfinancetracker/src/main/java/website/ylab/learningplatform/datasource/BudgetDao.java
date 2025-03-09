package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Budget;

import java.util.HashMap;
import java.util.Optional;

public class BudgetDao implements Dao<Budget> {
    private static final BudgetDao INSTANCE = new BudgetDao();

    /**
     * Gets the single instance of BudgetDao.
     * @return the single instance of BudgetDao
     */
    public static BudgetDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, Budget> repository = new HashMap<>();


    /**
     * Finds a budget by its associated user id.
     *
     * @param userId the id of the user to find the budget for
     * @return an Optional containing the budget if found, otherwise an empty Optional
     */
    public Optional<Budget> get(long userId) {
        return Optional.ofNullable(repository.get(userId));
    }


    /**
     * Saves or updates the budget for the specified user.
     *
     * @param userId the ID of the user whose budget is being saved
     * @param budget the budget to save for the user
     */
    public void save(long userId, Budget budget) {
        repository.put(userId, budget);
    }

    /**
     * Retrieves all budgets stored in the repository.
     *
     * @return an Iterable containing all budgets in the repository
     */
    @Override
    public Iterable<Budget> getAll() {
        return repository.values();
    }

}
