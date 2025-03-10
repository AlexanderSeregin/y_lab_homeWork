package website.ylab.learningplatform.service.interfaces;

import website.ylab.learningplatform.model.User;

/**
 * Service interface for authentication operations
 */
public interface AuthService {
    /**
     * Hash password
     * @param password raw password
     * @return hashed password
     */
    String hashPassword(String password);
    
    /**
     * Verify password
     * @param password raw password
     * @param hashedPassword hashed password
     * @return true if password matches
     */
    boolean verifyPassword(String password, String hashedPassword);
    
    /**
     * Login user
     * @param username username
     * @param password raw password
     * @return authenticated user or null if authentication failed
     */
    User login(String username, String password);
}
