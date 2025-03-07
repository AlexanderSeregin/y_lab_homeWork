package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.TransactionDao;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionService  {
    public static void newTransaction(User user, boolean isIncome, BigDecimal amount, Category category, Date date, String description) {
        if (BigDecimal.ZERO.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (!isIncome && checkBalance(user, amount)) {
            throw new IllegalArgumentException("Not enough money");
        }
        if (isIncome) {
            user.setBalance(user.getBalance().add(amount));
        } else {
            user.setBalance(user.getBalance().subtract(amount));
        }
        TransactionDao.getInstance().save(new Transaction(user, isIncome, amount, category, date, description));
        BudgetService.checkBudget(user);
    }

    private static boolean checkBalance(User user, BigDecimal amount) {
        return user.getBalance().compareTo(amount) > 0;
    }

    public static BigDecimal getSumOfUserTransactionsForCurrentMonth(User user) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = calendar.getTime();

        List<Transaction> transactions = TransactionDao.getInstance().getTransactionsByUser(user);

        return transactions.stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
