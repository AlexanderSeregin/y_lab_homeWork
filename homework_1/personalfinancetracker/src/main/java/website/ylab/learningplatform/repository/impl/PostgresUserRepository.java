package website.ylab.learningplatform.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import website.ylab.learningplatform.config.DatabaseConfig;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of UserRepository
 */
@Repository
public class PostgresUserRepository extends PostgresRepository<User, Long> implements UserRepository {
    private static final String SELECT_BY_ID = "SELECT * FROM finance_schema.users WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM finance_schema.users";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM finance_schema.users WHERE username = ?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM finance_schema.users WHERE email = ?";
    private static final String INSERT = "INSERT INTO finance_schema.users (id, username, email, password_hash, is_admin, is_blocked, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE finance_schema.users SET username = ?, email = ?, password_hash = ?, is_admin = ?, is_blocked = ?, balance = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM finance_schema.users WHERE id = ?";

    @Autowired
    public PostgresUserRepository(DatabaseConfig dbConfig) {
        super(dbConfig);
    }


    /**
     * Get singleton instance
     *
     * @return repository instance
     */

    @Override
    public Optional<User> findById(Long id) {
        return querySingle(SELECT_BY_ID, this::mapResultSetToUser, id);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            Long userId = getNextSequenceValue("service_schema.user_seq");
            user.setId(userId);

            executeUpdate(INSERT,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.isAdmin(),
                    user.isBlocked(),
                    user.getBalance());
        } else {
            executeUpdate(UPDATE,
                    user.getUsername(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.isAdmin(),
                    user.isBlocked(),
                    user.getBalance(),
                    user.getId());
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return queryList(SELECT_ALL, this::mapResultSetToUser);
    }

    @Override
    public void delete(User user) {
        if (user.getId() != null) {
            deleteById(user.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        executeUpdate(DELETE, id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return querySingle(SELECT_BY_USERNAME, this::mapResultSetToUser, username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return querySingle(SELECT_BY_EMAIL, this::mapResultSetToUser, email);
    }

    /**
     * Map database result to User entity
     *
     * @param rs ResultSet containing user data
     * @return mapped User entity
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getBoolean("is_admin"),
                rs.getBoolean("is_blocked"),
                rs.getBigDecimal("balance")
        );
        user.setId(rs.getLong("id"));
        return user;
    }
}
