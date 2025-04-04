package website.ylab.learningplatform.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import website.ylab.learningplatform.config.DatabaseConfig;
import website.ylab.learningplatform.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base PostgreSQL repository implementation
 *
 * @param <T>  entity type
 * @param <ID> entity ID type
 */
public abstract class PostgresRepository<T, ID> implements Repository<T, ID> {
    private DatabaseConfig dbConfig;

    @Autowired
    protected PostgresRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    protected PostgresRepository() {
    }

    /**
     * Get a database connection
     *
     * @return database connection
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        return dbConfig.getConnection();
    }

    /**
     * Execute a query that returns a single object
     *
     * @param sql       SQL query
     * @param rowMapper function to map ResultSet to entity
     * @param params    query parameters
     * @return optional containing the entity or empty if not found
     */
    protected Optional<T> querySingle(String sql, RowMapper<T> rowMapper, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    /**
     * Execute a query that returns multiple objects
     *
     * @param sql       SQL query
     * @param rowMapper function to map ResultSet to entity
     * @param params    query parameters
     * @return list of entities
     */
    protected List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params) {
        List<T> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

        return results;
    }

    /**
     * Execute an update query (INSERT, UPDATE, DELETE)
     *
     * @param sql    SQL query
     * @param params query parameters
     * @return number of affected rows
     */
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);

            return ps.executeUpdate();
        } catch (SQLException e) {

            throw new RuntimeException("Database update failed", e);
        }
    }

    /**
     * Execute an insert query and return the generated ID
     *
     * @param sql    SQL query
     * @param params query parameters
     * @return generated ID
     */
    protected long executeInsertWithId(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            setParameters(ps, params);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new SQLException("Creating entity failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database insert failed", e);
        }
    }

    /**
     * Execute a query to get the next value from a sequence
     *
     * @param sequenceName the name of the sequence
     * @return next value from the sequence
     */
    protected long getNextSequenceValue(String sequenceName) {
        String sql = String.format("SELECT nextval('%s')", sequenceName);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Failed to get next sequence value");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get next sequence value", e);
        }
    }

    /**
     * Set parameters for a prepared statement
     *
     * @param ps     prepared statement
     * @param params parameters to set
     * @throws SQLException if parameter setting fails
     */
    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    /**
     * Interface for mapping a database row to an entity
     *
     * @param <T> entity type
     */
    protected interface RowMapper<T> {
        /**
         * Map current row of ResultSet to entity
         *
         * @param rs result set positioned at the current row
         * @return mapped entity
         * @throws SQLException if mapping fails
         */
        T mapRow(ResultSet rs) throws SQLException;
    }
}
