package website.ylab.learningplatform.service;

//import website.ylab.learningplatform.datasource.BudgetDao;
//import website.ylab.learningplatform.datasource.PostgresBudgetDao;

import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresBudgetRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class BudgetService {


    public static void checkBudget(User user) {
        BigDecimal sum = TransactionService.getSumOfUserSpendingsInCurrentMonth(user);
        Optional<Budget> budgetOptional = PostgresBudgetRepository.getInstance().findByUserId(user.getId());
        if (budgetOptional.isEmpty()) {
            return;
        }
        BigDecimal budgetAmount = budgetOptional.get().getAmount();
        if (sum.compareTo(budgetAmount) > 0) {
            NotificationService.sendNotification(user, "You have exceeded your budget for the month!");
        }
    }

    public static BigDecimal getBudget(User user) {
        Optional<Budget> budgetOptional = PostgresBudgetRepository.getInstance().findByUserId(user.getId());
        if (budgetOptional.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return budgetOptional.get().getAmount();
    }

    public static void setBudget(User user, BigDecimal newBudget) {
        PostgresBudgetRepository.getInstance().save(new Budget(user.getId(), newBudget));
        //PostgresBudgetDao.getInstance().save(user.getId(), new Budget(user.getId(), newBudget));
    }
}
