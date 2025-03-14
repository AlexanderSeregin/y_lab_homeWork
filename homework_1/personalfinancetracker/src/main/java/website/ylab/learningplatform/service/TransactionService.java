package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresGoalRepository;
import website.ylab.learningplatform.repository.impl.PostgresTransactionRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    public static void newTransaction(User user, boolean isIncome, BigDecimal amount, Category category, Date date, String description) {
        System.out.println("TransactionService.newTransaction");
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
        PostgresTransactionRepository.getInstance().save(new Transaction(user.getId(), isIncome, description, amount, category, date));
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

        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(user.getId());
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Iterable<Transaction> getUserTransactions(User user) {
        return PostgresTransactionRepository.getInstance().findByUserId(user.getId()).orElse(null);
    }

    public static void changeDescription(User user, long transactionId, String newDescription) {
        PostgresTransactionRepository ptr = PostgresTransactionRepository.getInstance();
        ptr.save(new Transaction(transactionId, ptr.findById(transactionId).get().isIncome(), newDescription, ptr.findById(transactionId).get().getAmount(), ptr.findById(transactionId).get().getCategory(), ptr.findById(transactionId).get().getDate()));
    }

    public static void changeAmount(User user, long transactionId, BigDecimal newAmount) {
        BigDecimal oldAmount = PostgresTransactionRepository.getInstance().findById(transactionId).get().getAmount();
        PostgresTransactionRepository ptr = PostgresTransactionRepository.getInstance();
        ptr.save(new Transaction(transactionId, ptr.findById(transactionId).get().isIncome(), ptr.findById(transactionId).get().getDescription(), newAmount, ptr.findById(transactionId).get().getCategory(), ptr.findById(transactionId).get().getDate()));
        user.setBalance(user.getBalance().subtract(newAmount.subtract(oldAmount)));
    }

    public static void changeCategory(User user, long transactionId, Category newCategory) {
        PostgresTransactionRepository ptr = PostgresTransactionRepository.getInstance();
        ptr.save(new Transaction(transactionId, ptr.findById(transactionId).get().isIncome(), ptr.findById(transactionId).get().getDescription(), ptr.findById(transactionId).get().getAmount(), newCategory, ptr.findById(transactionId).get().getDate()));
    }

    public static void deleteTransaction(User user, long transactionToDeleteId) {
        BigDecimal amount = PostgresTransactionRepository.getInstance().findById(transactionToDeleteId).get().getAmount();
        PostgresTransactionRepository.getInstance().deleteById(transactionToDeleteId);
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

        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(user.getId());
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.get().stream()
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

        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(user.getId());
        if (transactions == null) {
            return null;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .filter(t -> !t.isIncome())
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
    }

    public static BigDecimal getSumOfUserSpendingsForPeriod(User user, Date from, Date to) {
        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(user.getId());
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(from) || t.getDate().equals(from))
                .filter(t -> t.getDate().before(to) || t.getDate().equals(to))
                .filter(t -> !t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getSumOfUserIncomeForPeriod(User user, Date from, Date to) {
        Optional<List<Transaction>> transactions = PostgresTransactionRepository.getInstance().findByUserId(user.getId());
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(from) || t.getDate().equals(from))
                .filter(t -> t.getDate().before(to) || t.getDate().equals(to))
                .filter(t -> t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
