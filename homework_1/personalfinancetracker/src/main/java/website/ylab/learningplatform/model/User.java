package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class User {
    private static long counter = 0;
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private final boolean isAdmin;
    private boolean isBlocked;
    private BigDecimal balance;

    public User(String username, String email, String passwordHash) {
        this.id = ++counter;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = false;
        this.isBlocked = false;
        this.balance = BigDecimal.ZERO;
    }

    public User(String username, String email, String passwordHash, boolean isAdmin, boolean isBlocked, BigDecimal balance) {
        this.id = ++counter;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }

    /**
     * Checks if the user has enough balance for a withdrawal
     * @param amount amount to check
     * @return true if user has enough balance
     */
    public boolean hasEnoughBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }
    
    /**
     * Add amount to user balance
     * @param amount amount to add (positive for deposit, negative for withdrawal)
     * @throws IllegalArgumentException if trying to withdraw more than available balance
     */
    public void updateBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0 && !hasEnoughBalance(amount.abs())) {
            throw new IllegalArgumentException("Insufficient funds: available " + balance + ", trying to withdraw " + amount.abs());
        }
        this.balance = this.balance.add(amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the balance of the user.
     *
     * @return the balance of the user
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Returns true if the user is blocked, false otherwise.
     *
     * @return true if the user is blocked, false otherwise
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Returns true if the user is an administrator, false otherwise.
     *
     * @return true if the user is an administrator, false otherwise
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the blocked status of the user.
     *
     * @param blocked true if the user should be blocked, false otherwise
     */
    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setName(String newName) {
        this.username = newName;
    }

    public void setIsBlocked(boolean b) {
        this.isBlocked = b;
    }

    public String getName() {
        return username;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }
}
