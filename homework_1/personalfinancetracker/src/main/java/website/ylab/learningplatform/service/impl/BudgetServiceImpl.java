package website.ylab.learningplatform.service.impl;

import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.repository.BudgetRepository;
import website.ylab.learningplatform.repository.impl.PostgresBudgetRepository;
import website.ylab.learningplatform.service.interfaces.BudgetService;
import website.ylab.learningplatform.service.interfaces.TransactionService;

import java.math.BigDecimal;

public class BudgetServiceImpl implements BudgetService {
    private static final BudgetServiceImpl INSTANCE = new BudgetServiceImpl();
    private final BudgetRepository budgetRepository;
    private TransactionService transactionService; // Will be set after initialization to avoid circular dependency

    private BudgetServiceImpl() {
        this.budgetRepository = PostgresBudgetRepository.getInstance();
    }

    public static BudgetServiceImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Set transaction service - called after initialization to avoid circular dependency
     * @param transactionService transaction service
     */
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Budget getBudget(Long userId) {
        return budgetRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public Budget setBudget(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget amount must be positive");
        }
        
        Budget budget = budgetRepository.findByUserId(userId).orElse(new Budget(userId, amount));
        budget.setAmount(amount);
        return budgetRepository.save(budget);
    }

    @Override
    public boolean isBudgetExceeded(Long userId) {
        Budget budget = getBudget(userId);
        if (budget == null) {
            return false; // No budget set
        }

        BigDecimal monthlySpending = transactionService.getSumOfUserSpendingsInCurrentMonth(userId);
        return monthlySpending.compareTo(budget.getAmount()) > 0;
    }
}
