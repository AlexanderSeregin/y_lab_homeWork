package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.TransactionDao;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionService {
    public static void newTransaction(User user, boolean isIncome, BigDecimal amount, Category category, Date date, String description) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (!isIncome && checkBalance(user, amount)) {
            throw new IllegalArgumentException("Not enough money");
        }
        if (!isIncome) {
            amount = amount.negate();
        }
        user.setBalance(user.getBalance().add(amount));
        TransactionDao.getInstance().save(new Transaction(user.getId(), isIncome, description, amount, category, date));
        BudgetService.checkBudget(user);
    }

    private static boolean checkBalance(User user, BigDecimal amount) {
        return user.getBalance().compareTo(amount) < 0;
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

    public static Iterable<Transaction> getUserTransactions(User user) {
        return TransactionDao.getInstance().getTransactionsByUser(user);
    }

    public static void changeDescription(User user, long transactionId, String newDescription) {
        TransactionDao.getInstance().get(user.getId())
                .ifPresent(transactions -> transactions.stream()
                        .filter(t -> t.getId() == transactionId)
                        .findFirst()
                        .ifPresent(t -> t.setDescription(newDescription))
                );
    }

    public static void changeAmount(User user, long transactionId, BigDecimal newAmount) {
        BigDecimal oldAmount = TransactionDao.getInstance().get(user.getId())
                .map(transactions -> transactions.stream()
                        .filter(t -> t.getId() == transactionId)
                        .findFirst()
                        .map(Transaction::getAmount)
                        .orElse(BigDecimal.ZERO))
                .orElse(BigDecimal.ZERO);
        TransactionDao.getInstance().get(user.getId())
                .ifPresent(transactions -> transactions.stream()
                        .filter(t -> t.getId() == transactionId)
                        .findFirst()
                        .ifPresent(t -> t.setAmount(newAmount))
                );
        user.setBalance(user.getBalance().subtract(newAmount.subtract(oldAmount)));
    }

    public static void changeCategory(User user, long transactionId, Category newCategory) {
        TransactionDao.getInstance().get(user.getId())
                .ifPresent(transactions -> transactions.stream()
                        .filter(t -> t.getId() == transactionId)
                        .findFirst()
                        .ifPresent(t -> t.setCategory(newCategory))
                );
    }

    public static void deleteTransaction(User user, long transactionToDeleteId) {
        BigDecimal amount = TransactionDao.getInstance().get(user.getId())
                .map(transactions -> transactions.stream()
                        .filter(t -> t.getId() == transactionToDeleteId)
                        .findFirst()
                        .map(Transaction::getAmount)
                        .orElse(BigDecimal.ZERO))
                .orElse(BigDecimal.ZERO);
        TransactionDao.getInstance().get(user.getId())
                .ifPresent(transactions -> transactions.removeIf(t -> t.getId() == transactionToDeleteId));
        user.setBalance(user.getBalance().add(amount));
    }

    public static BigDecimal getSumOfUserSpendingsInCurrentMonth(User user) {
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
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .filter(t -> !t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Map<Category, BigDecimal> getSumOfUserSpendingsByCategoryForCurrentMonth(User user) {
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
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .filter(t -> !t.isIncome())
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
    }

    public static BigDecimal getSumOfUserSpendingsForPeriod(User user, Date from, Date to) {
        List<Transaction> transactions = TransactionDao.getInstance().getTransactionsByUser(user);
        if (transactions == null) {
            return BigDecimal.ZERO;
        }
        return transactions.stream()
                .filter(t -> t.getDate().after(from) || t.getDate().equals(from))
                .filter(t -> t.getDate().before(to) || t.getDate().equals(to))
                .filter(t -> !t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getSumOfUserIncomeForPeriod(User user, Date from, Date to) {
        List<Transaction> transactions = TransactionDao.getInstance().getTransactionsByUser(user);
        if (transactions == null) {
            return BigDecimal.ZERO;
        }
        return transactions.stream()
                .filter(t -> t.getDate().after(from) || t.getDate().equals(from))
                .filter(t -> t.getDate().before(to) || t.getDate().equals(to))
                .filter(t -> t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
