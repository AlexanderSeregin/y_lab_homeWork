package website.ylab.learningplatform.service;

import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.BudgetRepository;
import website.ylab.learningplatform.repository.impl.PostgresBudgetRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class BudgetService {
    private static final BudgetRepository budgetRepository = PostgresBudgetRepository.getInstance();

    public static void checkBudget(User user) {
        BigDecimal sum = TransactionService.getSumOfUserSpendingsInCurrentMonth(user);
        Optional<Budget> budgetOptional = budgetRepository.findByUserId(user.getId());
        if (budgetOptional.isEmpty()) {
            return;
        }
        BigDecimal budgetAmount = budgetOptional.get().getAmount();
        if (sum.compareTo(budgetAmount) > 0) {
            NotificationService.sendNotification(user, "You have exceeded your budget for the month!");
        }
    }

    public static BigDecimal getBudget(User user) {
        Optional<Budget> budgetOptional = budgetRepository.findByUserId(user.getId());
        if (budgetOptional.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return budgetOptional.get().getAmount();
    }

    public static void setBudget(User user, BigDecimal newBudget) {
        budgetRepository.save(new Budget(user.getId(), newBudget));
    }
}
