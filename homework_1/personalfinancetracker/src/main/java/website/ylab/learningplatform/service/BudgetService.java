package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.BudgetDao;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.Optional;

public class BudgetService {


    public static void checkBudget(User user) {
        BigDecimal sum = TransactionService.getSumOfUserSpendingsInCurrentMonth(user);
        Optional<Budget> budgetOptional = BudgetDao.getInstance().get(user.getId());
        if (budgetOptional.isEmpty()) {
            return;
        }
        BigDecimal budgetAmount = budgetOptional.get().getAmount();
        if (sum.compareTo(budgetAmount) > 0) {
            NotificationService.sendNotification(user, "You have exceeded your budget for the month!");
        }
    }

    public static BigDecimal getBudget(User user) {
        Optional<Budget> budgetOptional = BudgetDao.getInstance().get(user.getId());
        if (budgetOptional.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return budgetOptional.get().getAmount();
    }

    public static void setBudget(User user, BigDecimal newBudget) {
        BudgetDao.getInstance().save(user.getId(), new Budget(user.getId(), newBudget));
    }
}
