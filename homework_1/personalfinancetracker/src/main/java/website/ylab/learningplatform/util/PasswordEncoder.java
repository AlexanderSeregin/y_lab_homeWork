package website.ylab.learningplatform.util;

import java.util.Base64;

/**
 * Utility class for encoding passwords.
 * This class provides methods to encode passwords securely.
 */
public class PasswordEncoder {
    
    private static final String SALT = "personalFinanceApp";
    
    /**
     * Encodes a password using Base64 encoding with a salt.
     *
     * @param rawPassword the raw password to encode
     * @return the encoded password
     */
    public String encode(String rawPassword) {
        return Base64.getEncoder().encodeToString((SALT + rawPassword).getBytes());
    }
    
    /**
     * Checks if a raw password matches an encoded password.
     *
     * @param rawPassword the raw password to check
     * @param encodedPassword the encoded password to check against
     * @return true if the passwords match, false otherwise
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
