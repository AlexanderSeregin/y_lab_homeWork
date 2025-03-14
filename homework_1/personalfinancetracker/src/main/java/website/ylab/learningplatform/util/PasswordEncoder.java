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
    public static String encode(String rawPassword) {
        return Base64.getEncoder().encodeToString((SALT + rawPassword).getBytes());
    }

}
