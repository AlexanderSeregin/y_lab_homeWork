package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.datasource.PostgresBudgetDao;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.repository.BudgetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of BudgetRepository
 */
public class PostgresBudgetRepository extends PostgresRepository<Budget, Long> implements BudgetRepository {
    private static final PostgresBudgetRepository INSTANCE = new PostgresBudgetRepository();

    private final PostgresBudgetDao budgetDao;

    private PostgresBudgetRepository() {
        // Private constructor to enforce singleton pattern
        this.budgetDao = PostgresBudgetDao.getInstance();
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static PostgresBudgetRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Budget> findById(Long id) {
        // In this simplified model, the id is actually the userId
        return findByUserId(id);
    }

    @Override
    public Budget save(Budget budget) {
        budgetDao.save(budget.getUserId(), budget);
        return budget;
    }

    @Override
    public List<Budget> findAll() {
        List<Budget> budgets = new ArrayList<>();
        budgetDao.getAll().forEach(budgets::add);
        return budgets;
    }

    @Override
    public void delete(Budget budget) {
        budgetDao.delete(budget.getUserId());
    }

    @Override
    public void deleteById(Long id) {
        // In this simplified model, the id is actually the userId
        budgetDao.delete(id);
    }

    @Override
    public Optional<Budget> findByUserId(Long userId) {
        return budgetDao.get(userId);
    }
}
