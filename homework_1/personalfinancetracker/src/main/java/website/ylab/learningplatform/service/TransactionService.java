package website.ylab.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.TransactionRepository;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.service.BudgetService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, 
                             UserRepository userRepository,
                             BudgetService budgetService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.budgetService = budgetService;
    }

    public Transaction createTransaction(Transaction transaction) {
        User user = userRepository.findById(transaction.getUserId()).orElseThrow(() -> 
            new IllegalArgumentException("User not found"));
            
        if (BigDecimal.ZERO.compareTo(transaction.getAmount()) > 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        
        BigDecimal amount = transaction.getAmount();
        if (!transaction.isIncome() && checkBalance(user, amount)) {
            throw new IllegalArgumentException("Not enough money");
        }
        
        if (!transaction.isIncome()) {
            amount = amount.negate();
            transaction.setAmount(amount);
        }
        
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        budgetService.checkBudget(user);
        
        return savedTransaction;
    }

    private boolean checkBalance(User user, BigDecimal amount) {
        return user.getBalance().compareTo(amount) < 0;
    }

    public BigDecimal getSumOfUserTransactionsForCurrentMonth(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> 
            new IllegalArgumentException("User not found"));
            
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

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId).orElse(new ArrayList<>());
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    public Transaction updateTransactionDescription(Long transactionId, String newDescription) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setDescription(newDescription);
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransactionAmount(Long userId, Long transactionId, BigDecimal newAmount) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
            
        BigDecimal oldAmount = transaction.getAmount();
        transaction.setAmount(newAmount);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        user.setBalance(user.getBalance().subtract(oldAmount).add(newAmount));
        userRepository.save(user);
        
        return updatedTransaction;
    }

    public Transaction updateTransactionCategory(Long transactionId, Category newCategory) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setCategory(newCategory);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Transaction transaction) {
        User user = userRepository.findById(transaction.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BigDecimal amount = transaction.getAmount();
        transactionRepository.delete(transaction);
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    public BigDecimal getSumOfUserSpendingsInCurrentMonth(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
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
                .filter(t -> !t.isIncome())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Category, BigDecimal> getSumOfUserSpendingsByCategoryForCurrentMonth(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
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
            return new HashMap<>();
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(startOfMonth) || t.getDate().equals(startOfMonth))
                .filter(t -> !t.isIncome())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, t -> t.getAmount().abs(), BigDecimal::add)
                ));
    }

    public BigDecimal getSumOfUserSpendingsForPeriod(Long userId, Date from, Date to) {
        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(userId);
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

    public BigDecimal getSumOfUserIncomeForPeriod(Long userId, Date from, Date to) {
        Optional<List<Transaction>> transactions = transactionRepository.findByUserId(userId);
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return transactions.get().stream()
                .filter(t -> t.getDate().after(from) || t.getDate().equals(from))
                .filter(t -> t.getDate().before(to) || t.getDate().equals(to))
                .filter(Transaction::isIncome)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
