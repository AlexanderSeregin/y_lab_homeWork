package website.ylab.learningplatform.repository;

import website.ylab.learningplatform.model.User;

import java.util.Optional;

/**
 * Repository interface for User entity
 */
public interface UserRepository extends Repository<User, Long> {
    /**
     * Find user by username
     * @param username username
     * @return Optional containing user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * @param email email
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);
}
