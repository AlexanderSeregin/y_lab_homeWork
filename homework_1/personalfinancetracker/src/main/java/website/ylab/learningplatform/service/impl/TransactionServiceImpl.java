package website.ylab.learningplatform.service.impl;

import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.TransactionRepository;
import website.ylab.learningplatform.repository.UserRepository;


import website.ylab.learningplatform.repository.impl.PostgresTransactionRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;
import website.ylab.learningplatform.service.interfaces.BudgetService;
import website.ylab.learningplatform.service.interfaces.TransactionService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    private static final TransactionServiceImpl INSTANCE = new TransactionServiceImpl();
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    private TransactionServiceImpl() {
        // В реальном приложении было бы использовано Dependency Injection
        this.transactionRepository = PostgresTransactionRepository.getInstance();
        this.userRepository = PostgresUserRepository.getInstance();
        this.budgetService = BudgetServiceImpl.getInstance();
    }

    public static TransactionServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Transaction createTransaction(Long userId, boolean isIncome, BigDecimal amount, Category category, Date date, String description) {
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Transaction transaction;
        if (isIncome) {
            transaction = Transaction.createIncome(userId, description, amount, category, date);
            user.updateBalance(amount);
        } else {
            if (!user.hasEnoughBalance(amount)) {
                throw new IllegalArgumentException("Not enough money in account");
            }
            transaction = Transaction.createExpense(userId, description, amount, category, date);
            user.updateBalance(transaction.getAmount());
        }

        transactionRepository.save(transaction);
        userRepository.save(user);


        budgetService.isBudgetExceeded(userId);
        
        return transaction;
    }

    @Override
    public Optional<List<Transaction>> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public Transaction updateDescription(Long userId, Long transactionId, String newDescription) {
        Transaction transaction = findUserTransaction(userId, transactionId);
        transaction.setDescription(newDescription);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateAmount(Long userId, Long transactionId, BigDecimal newAmount) {
        Transaction transaction = findUserTransaction(userId, transactionId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        // Calculate the difference to update user balance
        BigDecimal difference;
        if (transaction.isIncome()) {
            if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Income amount must be positive");
            }
            difference = newAmount.subtract(transaction.getAmount());
        } else {
            if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Expense amount must be positive");
            }
            // For expenses, we need to negate the amount and calculate difference
            BigDecimal oldAbsAmount = transaction.getAbsoluteAmount();
            difference = oldAbsAmount.subtract(newAmount).negate(); // If new amount is higher, difference is negative
        }

        // Check if user has enough balance for the new amount (if it's an expense and amount is increased)
        if (!transaction.isIncome() && difference.compareTo(BigDecimal.ZERO) < 0) {
            if (!user.hasEnoughBalance(difference.abs())) {
                throw new IllegalArgumentException("Not enough balance for the increased expense");
            }
        }

        // Update user balance
        user.updateBalance(difference);
        userRepository.save(user);

        // Update transaction amount
        if (transaction.isIncome()) {
            transaction.setAmount(newAmount);
        } else {
            transaction.setAmount(newAmount.negate());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateCategory(Long userId, Long transactionId, Category newCategory) {
        Transaction transaction = findUserTransaction(userId, transactionId);
        transaction.setCategory(newCategory);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = findUserTransaction(userId, transactionId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        // Reverse the transaction effect on user balance
        BigDecimal reversalAmount = transaction.getAmount().negate();
        user.updateBalance(reversalAmount);
        userRepository.save(user);

        transactionRepository.deleteById(transactionId);
    }

    @Override
    public BigDecimal getSumOfUserSpendingsInCurrentMonth(Long userId) {
        Date now = new Date();
        Date startOfMonth = getStartOfMonth(now);

        return transactionRepository.findByUserIdAndDateBetween(userId, startOfMonth, now).stream()
                .filter(t -> !t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs(); // Make the result positive for display purposes
    }

    @Override
    public Map<Category, BigDecimal> getSumOfUserSpendingsByCategoryForCurrentMonth(Long userId) {
        Date now = new Date();
        Date startOfMonth = getStartOfMonth(now);

        return transactionRepository.findByUserIdAndDateBetween(userId, startOfMonth, now).stream()
                .filter(t -> !t.isIncome())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                t -> t.getAmount().abs(), // Make amounts positive for display
                                BigDecimal::add)
                ));
    }

    @Override
    public BigDecimal getSumOfUserSpendingsForPeriod(Long userId, Date from, Date to) {
        return transactionRepository.findByUserIdAndDateBetween(userId, from, to).stream()
                .filter(t -> !t.isIncome())
                .map(t -> t.getAmount().abs()) // Make amounts positive for display
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getSumOfUserIncomeForPeriod(Long userId, Date from, Date to) {
        return transactionRepository.findByUserIdAndDateBetween(userId, from, to).stream()
                .filter(Transaction::isIncome)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getSumOfUserTransactionsForCurrentMonth(Long userId) {
        Date now = new Date();
        Date startOfMonth = getStartOfMonth(now);

        return transactionRepository.findByUserIdAndDateBetween(userId, startOfMonth, now).stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Find a transaction that belongs to a specific user
     * @param userId user ID
     * @param transactionId transaction ID
     * @return transaction if found
     * @throws NoSuchElementException if transaction not found or doesn't belong to user
     */
    private Transaction findUserTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found: " + transactionId));

        if (!transaction.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Transaction does not belong to user");
        }

        return transaction;
    }

    /**
     * Get start of month for a given date
     * @param date date
     * @return start of month date
     */
    private Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
