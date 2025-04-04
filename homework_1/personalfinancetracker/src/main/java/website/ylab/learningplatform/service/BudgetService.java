package website.ylab.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.BudgetRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final NotificationService notificationService;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                         NotificationService notificationService) {
        this.budgetRepository = budgetRepository;
        this.notificationService = notificationService;
    }

    public void checkBudget(User user, BigDecimal sum) {
        Optional<Budget> budgetOptional = budgetRepository.findByUserId(user.getId());
        if (budgetOptional.isEmpty()) {
            return;
        }
        BigDecimal budgetAmount = budgetOptional.get().getAmount();
        if (sum.compareTo(budgetAmount) > 0) {
            notificationService.sendNotification(user, "You have exceeded your budget for the month!");
        }
    }

    public BigDecimal getBudget(Long userId) {
        Optional<Budget> budgetOptional = budgetRepository.findByUserId(userId);
        if (budgetOptional.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return budgetOptional.get().getAmount();
    }

    public Budget setBudget(Long userId, BigDecimal newBudget) {
        return budgetRepository.save(new Budget(userId, newBudget));
    }

    public Budget getUserBudget(Long userId) {
        Optional<Budget> budgetOptional = budgetRepository.findByUserId(userId);
        if (budgetOptional.isEmpty()) {
            return null;
        }
        return budgetOptional.get();
    }

    public Budget updateBudget(Budget budget) {
        return budgetRepository.save(budget);
    }
}
