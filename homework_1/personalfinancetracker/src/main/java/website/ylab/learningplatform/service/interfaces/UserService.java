package website.ylab.learningplatform.service.interfaces;

import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for user management operations
 */
public interface UserService {
    /**
     * Find user by id
     * @param userId user id
     * @return user if found, null otherwise
     */
    User findById(Long userId);
    
    /**
     * Authenticate user with username and password
     * @param username username
     * @param password raw password
     * @return authenticated user or null if authentication failed
     */
    User authenticate(String username, String password);
    
    /**
     * Register new user
     * @param username username
     * @param email email
     * @param password raw password
     * @param isAdmin is admin flag
     * @param initialBalance initial balance
     * @return registered user
     */
    User register(String username, String email, String password, boolean isAdmin, BigDecimal initialBalance);
    
    /**
     * Update user profile
     * @param userId user id
     * @param username new username
     * @param email new email
     * @return updated user
     */
    User updateProfile(Long userId, String username, String email);
    
    /**
     * Change user password
     * @param userId user id
     * @param oldPassword old password
     * @param newPassword new password
     * @return true if password changed successfully
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * Delete user account
     * @param userId user id
     */
    void deleteAccount(Long userId);
    
    /**
     * Find all users
     * @return list of all users
     */
    List<User> findAll();
}
