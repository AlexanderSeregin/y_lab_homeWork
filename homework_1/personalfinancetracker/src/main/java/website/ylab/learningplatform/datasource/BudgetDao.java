package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Budget;

import java.util.HashMap;
import java.util.Optional;

public class BudgetDao implements Dao<Budget> {
    private static final BudgetDao INSTANCE = new BudgetDao();

    public static BudgetDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, Budget> repository = new HashMap<>();



    public Optional<Budget> get(long userId) {
        return Optional.ofNullable(repository.get(userId));
    }


    public void save(long userId, Budget budget) {
        repository.put(userId, budget);
    }
}
