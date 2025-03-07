package website.ylab.learningplatform.model;

import java.math.BigDecimal;

public class User {
    private static long counter = 0;
    private final long id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isBlocked;
    private BigDecimal balance;

    public User(String name, String email, String password) {
        this.id = ++counter;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
        this.isBlocked = false;
        this.balance = new BigDecimal(0);
    }

    // getters / setters / toString()

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Object getPassword() {
        return password;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
