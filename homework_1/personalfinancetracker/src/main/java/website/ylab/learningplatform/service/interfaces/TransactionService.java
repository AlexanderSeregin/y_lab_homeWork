package website.ylab.learningplatform.service.interfaces;

import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service interface for transaction management operations
 */
public interface TransactionService {
    /**
     * Create new transaction
     * @param userId user id
     * @param isIncome is income flag
     * @param amount transaction amount
     * @param category transaction category
     * @param date transaction date
     * @param description transaction description
     * @return created transaction
     */
    Transaction createTransaction(Long userId, boolean isIncome, BigDecimal amount, 
                                Category category, Date date, String description);
    
    /**
     * Get all transactions for user
     * @param userId user id
     * @return list of user transactions
     */
    List<Transaction> getUserTransactions(Long userId);
    
    /**
     * Update transaction description
     * @param userId user id
     * @param transactionId transaction id
     * @param newDescription new description
     * @return updated transaction
     */
    Transaction updateDescription(Long userId, Long transactionId, String newDescription);
    
    /**
     * Update transaction amount
     * @param userId user id
     * @param transactionId transaction id
     * @param newAmount new amount
     * @return updated transaction
     */
    Transaction updateAmount(Long userId, Long transactionId, BigDecimal newAmount);
    
    /**
     * Update transaction category
     * @param userId user id
     * @param transactionId transaction id
     * @param newCategory new category
     * @return updated transaction
     */
    Transaction updateCategory(Long userId, Long transactionId, Category newCategory);
    
    /**
     * Delete transaction
     * @param userId user id
     * @param transactionId transaction id
     */
    void deleteTransaction(Long userId, Long transactionId);
    
    /**
     * Get sum of user spendings for current month
     * @param userId user id
     * @return sum of spendings
     */
    BigDecimal getSumOfUserSpendingsInCurrentMonth(Long userId);
    
    /**
     * Get sum of user spendings by category for current month
     * @param userId user id
     * @return map of category to sum
     */
    Map<Category, BigDecimal> getSumOfUserSpendingsByCategoryForCurrentMonth(Long userId);
    
    /**
     * Get sum of user spendings for period
     * @param userId user id
     * @param from start date
     * @param to end date
     * @return sum of spendings
     */
    BigDecimal getSumOfUserSpendingsForPeriod(Long userId, Date from, Date to);
    
    /**
     * Get sum of user income for period
     * @param userId user id
     * @param from start date
     * @param to end date
     * @return sum of income
     */
    BigDecimal getSumOfUserIncomeForPeriod(Long userId, Date from, Date to);
    
    /**
     * Get sum of user transactions for current month
     * @param userId user id
     * @return sum of transactions
     */
    BigDecimal getSumOfUserTransactionsForCurrentMonth(Long userId);
}
