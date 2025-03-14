package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.TransactionRepository;
import website.ylab.learningplatform.repository.UserRepository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of TransactionRepository
 */
public class PostgresTransactionRepository extends PostgresRepository<Transaction, Long> implements TransactionRepository {
    private static final PostgresTransactionRepository INSTANCE = new PostgresTransactionRepository();

    // SQL queries
    private static final String SELECT_BY_ID = "SELECT * FROM finance_schema.transactions WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM finance_schema.transactions";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM finance_schema.transactions WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO finance_schema.transactions (id, user_id, is_income, category, amount, description) VALUES (?,? ,?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE finance_schema.transactions SET user_id = ?, category = ?, amount = ?, description = ?, transaction_date = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM finance_schema.transactions WHERE id = ?";

   // private final UserRepository userRepository;

    private PostgresTransactionRepository() {
        // Private constructor to enforce singleton pattern
        //this.userRepository = PostgresUserRepository.getInstance();
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static PostgresTransactionRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return querySingle(SELECT_BY_ID, this::mapResultSetToTransaction, id);
    }

    @Override
    public Transaction save(Transaction transaction) {
        System.out.println("Saving transaction: " + transaction);
        if (transaction.getId() == null) {
            // Get next value from sequence for new transactions
            Long transactionId = getNextSequenceValue("service_schema.transaction_seq");
            transaction.setId(transactionId);
            System.out.println("Saving transaction: " + transaction);
            System.out.println(INSERT +
                    transaction.getId() +
                    transaction.getUserId() +
                    transaction.isIncome() +
                    transaction.getCategory()+
                    transaction.getAmount()+
                    transaction.getDescription());
            executeUpdate(INSERT, 
                    transaction.getId(),
                    transaction.getUserId(),
                    transaction.isIncome(),
                    transaction.getCategory().toString(),
                    transaction.getAmount(),
                    transaction.getDescription())
                    ;
        } else {
            executeUpdate(UPDATE,
                    transaction.getId(),
                    transaction.getUserId(),
                    transaction.isIncome(),
                    transaction.getCategory().toString(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getDate().getTime());
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return queryList(SELECT_ALL, this::mapResultSetToTransaction);
    }

    @Override
    public void delete(Transaction transaction) {
        if (transaction.getId() != null) {
            deleteById(transaction.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        executeUpdate(DELETE, id);
    }

    @Override
    public Optional<List<Transaction>> findByUserId(Long userId) {
        return Optional.of(queryList(SELECT_BY_USER_ID, this::mapResultSetToTransaction, userId));
    }

    @Override
    public List<Transaction> findByUserIdAndDateBetween(Long userId, Date fromDate, Date toDate) {
        return queryList(SELECT_BY_USER_ID + " AND transaction_date BETWEEN ? AND ?", this::mapResultSetToTransaction, userId, fromDate, toDate);
    }

    /**
     * Map database result to Transaction entity
     * @param rs ResultSet containing transaction data
     * @return mapped Transaction entity
     * @throws SQLException if mapping fails
     */
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        // Get user from user repository
        Long userId = rs.getLong("user_id");
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new SQLException("User not found for ID: " + userId));
        
        // Create category
//        Category category = new Category();
//        category.setId(rs.getLong("category"));
//        category.setName(rs.getString("category_name"));
//        category.setExpense(rs.getBoolean("category_is_expense"));
        
        // Create transaction
        Transaction transaction = new Transaction(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getBoolean("is_income"),
                rs.getString("description"),
                rs.getBigDecimal("amount"),
                Category.valueOf(rs.getString("category")),
                new Date(rs.getTimestamp("transaction_date").getTime())
        );
        transaction.setId(rs.getLong("id"));
        
        return transaction;
    }
}
