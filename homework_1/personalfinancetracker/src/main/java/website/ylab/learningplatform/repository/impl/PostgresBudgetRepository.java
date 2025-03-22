package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.config.DatabaseConfig;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.repository.BudgetRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of BudgetRepository
 */
public class PostgresBudgetRepository extends PostgresRepository<Budget, Long> implements BudgetRepository {
    private static final PostgresBudgetRepository INSTANCE = new PostgresBudgetRepository();
    private final DatabaseConfig databaseConfig;


    private PostgresBudgetRepository() {
        this.databaseConfig = DatabaseConfig.getInstance();
    }

    /**
     * Get singleton instance
     *
     * @return repository instance
     */
    public static PostgresBudgetRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Budget> findById(Long id) {
        return findByUserId(id);
    }

    @Override
    public Budget save(Budget budget) {
        String checkSql = "SELECT id FROM finance_schema.budgets WHERE user_id = ?";
        String insertSql = "INSERT INTO finance_schema.budgets (id, user_id, amount) VALUES (nextval('service_schema.budget_seq'), ?, ?)";
        String updateSql = "UPDATE finance_schema.budgets SET amount = ? WHERE user_id = ?";

        long userId = budget.getUserId();
        try (Connection conn = databaseConfig.getConnection()) {
            boolean exists = false;

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setLong(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    exists = rs.next();
                }
            }

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
        return budget;
    }

    @Override
    public List<Budget> findAll() {
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

    @Override
    public void delete(Budget budget) {
        String sql = "DELETE FROM finance_schema.budgets WHERE user_id = ?";
        long userId = budget.getUserId();
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            int rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting budget for user " + userId, e);
        }

    }

    @Override
    public void deleteById(Long userId) {
        String sql = "DELETE FROM finance_schema.budgets WHERE user_id = ?";
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            int rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting budget for user " + userId, e);
        }
    }

    @Override
    public Optional<Budget> findByUserId(Long userId) {
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
}
