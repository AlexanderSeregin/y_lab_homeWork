package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.config.DatabaseConfig;
import website.ylab.learningplatform.model.Budget;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of BudgetDao
 */
public class PostgresBudgetDao implements Dao<Budget> {
    private static final PostgresBudgetDao INSTANCE = new PostgresBudgetDao();
    private final DatabaseConfig databaseConfig;

    private PostgresBudgetDao() {
        this.databaseConfig = DatabaseConfig.getInstance();
    }

    /**
     * Gets the single instance of PostgresBudgetDao.
     * @return the single instance of PostgresBudgetDao
     */
    public static PostgresBudgetDao getInstance() {
        return INSTANCE;
    }

    /**
     * Finds a budget by its associated user id.
     *
     * @param userId the id of the user to find the budget for
     * @return an Optional containing the budget if found, otherwise an empty Optional
     */
    public Optional<Budget> get(long userId) {
        String sql = "SELECT user_id, amount FROM finance_schema.budgets WHERE user_id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal amount = rs.getBigDecimal("amount");
                    return Optional.of(new Budget(userId, amount));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving budget for user " + userId, e);
        }
    }

    /**
     * Saves or updates the budget for the specified user.
     *
     * @param userId the ID of the user whose budget is being saved
     * @param budget the budget to save for the user
     */
    public void save(long userId, Budget budget) {
        // Check if a budget already exists for this user
        String checkSql = "SELECT id FROM finance_schema.budgets WHERE user_id = ?";
        String insertSql = "INSERT INTO finance_schema.budgets (id, user_id, amount) VALUES (nextval('service_schema.budget_seq'), ?, ?)";
        String updateSql = "UPDATE finance_schema.budgets SET amount = ? WHERE user_id = ?";
        
        try (Connection conn = databaseConfig.getConnection()) {
            boolean exists = false;
            
            // Check if budget exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setLong(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    exists = rs.next();
                }
            }
            
            // Insert or update based on existence
            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setBigDecimal(1, budget.getAmount());
                    updateStmt.setLong(2, userId);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setLong(1, userId);
                    insertStmt.setBigDecimal(2, budget.getAmount());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving budget for user " + userId, e);
        }
    }

    /**
     * Deletes the budget for the specified user.
     * 
     * @param userId the ID of the user whose budget is being deleted
     * @return true if a budget was deleted, false otherwise
     */
    public boolean delete(long userId) {
        String sql = "DELETE FROM finance_schema.budgets WHERE user_id = ?";
        
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting budget for user " + userId, e);
        }
    }

    /**
     * Retrieves all budgets stored in the database.
     *
     * @return a List containing all budgets
     */
    @Override
    public Iterable<Budget> getAll() {
        String sql = "SELECT user_id, amount FROM finance_schema.budgets";
        List<Budget> budgets = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                long userId = rs.getLong("user_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                budgets.add(new Budget(userId, amount));
            }
            
            return budgets;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all budgets", e);
        }
    }
}
