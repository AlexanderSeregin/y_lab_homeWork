package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class User {
    private static long counter = 0;
    private long id;
    private String name;
    private String email;
    private String passwordHash;
    private final boolean isAdmin;
    private final boolean isBlocked;
    private BigDecimal balance;

    public User(String name, String email, String passwordHash) {
        this.id = ++counter;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = false;
        this.isBlocked = false;
        this.balance = new BigDecimal(0);
    }

    public User(String name, String email, String passwordHash, boolean isAdmin, boolean isBlocked, BigDecimal balance) {
        this.id = ++counter;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
        this.isBlocked = isBlocked;
        this.balance = balance;
    }

    // getters / setters / toString()

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Object getPasswordHash() {
        return passwordHash;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param newName the new name of the user
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Sets the password hash for the user.
     *
     * @param passwordHash the new password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
     * Sets the balance of the user.
     *
     * @param balance the balance of the user
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Returns true if the user is blocked, false otherwise.
     *
     * @return true if the user is blocked, false otherwise
     */
    public boolean getIsBlocked() {
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
     * @param isBlocked true if the user should be blocked, false otherwise
     */
    public void setIsBlocked(boolean isBlocked) {
        isBlocked = isBlocked;
    }

    /**
     * Sets the user's ID.
     *
     * @param id the ID to set
     */
    public void setId(long id) {
        this.id = id;
    }
}
