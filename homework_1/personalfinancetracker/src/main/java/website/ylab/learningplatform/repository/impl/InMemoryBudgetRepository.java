package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.repository.BudgetRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of BudgetRepository
 */
public class InMemoryBudgetRepository implements BudgetRepository {
    private static final InMemoryBudgetRepository INSTANCE = new InMemoryBudgetRepository();
    private final Map<Long, Budget> repository = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private InMemoryBudgetRepository() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static InMemoryBudgetRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Budget save(Budget budget) {
//        if (budget.getId() == null) {
//            budget.setId(idGenerator.incrementAndGet());
//        }
//        repository.put(budget.getId(), budget);
//        return budget;
        return null; //FIXME
    }

    @Override
    public List<Budget> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void delete(Budget budget) {
        //repository.remove(budget.getUserId()); //FIXME
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }

    @Override
    public Optional<Budget> findByUserId(Long userId) {
        return Optional.ofNullable(repository.get(userId));
    }
}
