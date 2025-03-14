package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.TransactionRepository;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.repository.impl.PostgresTransactionRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private static final TransactionRepository transactionRepository = PostgresTransactionRepository.getInstance();
    private static final UserRepository userRepository = PostgresUserRepository.getInstance();

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
        userRepository.save(user);
        transactionRepository.save(new Transaction(user.getId(), isIncome, description, amount, category, date));
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

        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(user.getId());
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Iterable<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByUserId(user.getId()).orElse(null);
    }

    public static void changeDescription(User user, long transactionId, String newDescription) {
        transactionRepository.save(new Transaction(transactionId, transactionRepository.findById(transactionId).get().isIncome(), newDescription, transactionRepository.findById(transactionId).get().getAmount(), transactionRepository.findById(transactionId).get().getCategory(), transactionRepository.findById(transactionId).get().getDate()));
    }

    public static void changeAmount(User user, long transactionId, BigDecimal newAmount) {
        BigDecimal oldAmount = transactionRepository.findById(transactionId).get().getAmount();
        transactionRepository.save(new Transaction(transactionId, transactionRepository.findById(transactionId).get().isIncome(), transactionRepository.findById(transactionId).get().getDescription(), newAmount, transactionRepository.findById(transactionId).get().getCategory(), transactionRepository.findById(transactionId).get().getDate()));
        user.setBalance(user.getBalance().subtract(newAmount.subtract(oldAmount)));
        userRepository.save(user);
    }

    public static void changeCategory(User user, long transactionId, Category newCategory) {
        transactionRepository.save(new Transaction(transactionId, transactionRepository.findById(transactionId).get().isIncome(), transactionRepository.findById(transactionId).get().getDescription(), transactionRepository.findById(transactionId).get().getAmount(), newCategory, transactionRepository.findById(transactionId).get().getDate()));
    }

    public static void deleteTransaction(User user, long transactionToDeleteId) {
        BigDecimal amount = transactionRepository.findById(transactionToDeleteId).get().getAmount();
        transactionRepository.deleteById(transactionToDeleteId);
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

        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(user.getId());
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

        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(user.getId());
        if (transactions == null) {
            return null;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .filter(t -> !t.isIncome())
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));
    }

    public static BigDecimal getSumOfUserSpendingsForPeriod(User user, Date from, Date to) {
        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(user.getId());
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
        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(user.getId());
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
