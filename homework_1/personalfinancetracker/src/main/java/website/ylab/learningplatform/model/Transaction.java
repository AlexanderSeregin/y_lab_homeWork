package website.ylab.learningplatform.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private Long id;
    private final Long userId;
    private boolean isIncome;
    private String description;
    private BigDecimal amount;
    private Category category;
    private final Date date;

    public Transaction(Long id, Long userId, boolean isIncome, String description, BigDecimal amount, Category category, Date date) {
        this.id = id;
        this.userId = userId;
        this.isIncome = isIncome;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date != null ? date : new Date();
    }

    public Transaction(Long userId, boolean isIncome, String description, BigDecimal amount, Category category, Date date) {
        this.userId = userId;
        this.isIncome = isIncome;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date != null ? date : new Date();
    }

    /**
     * Create a new income transaction
     * @param userId user ID
     * @param description description
     * @param amount positive amount
     * @param category category
     * @param date date
     * @return new income transaction
     */
    public static Transaction createIncome(Long userId, String description, BigDecimal amount, Category category, Date date) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Income amount must be positive");
        }
        return new Transaction(userId, true, description, amount, category, date);
    }

    /**
     * Create a new expense transaction
     * @param userId user ID
     * @param description description
     * @param amount positive amount (will be stored as negative)
     * @param category category
     * @param date date
     * @return new expense transaction
     */
    public static Transaction createExpense(Long userId, String description, BigDecimal amount, Category category, Date date) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        // Store expenses as negative values
        return new Transaction(userId, false, description, amount.negate(), category, date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get absolute amount (always positive)
     * @return absolute amount
     */
    public BigDecimal getAbsoluteAmount() {
        return amount.abs();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", isIncome=" + isIncome +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                '}';
    }

    public boolean isIncome() {
        return isIncome;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String toString(Transaction t) {
        return "Transaction{" +
                "id=" + t.getId() +
                ", userId=" + t.getUserId() +
                ", isIncome=" + t.isIncome() +
                ", description='" + t.getDescription() + '\'' +
                ", amount=" + t.getAmount() +
                ", category=" + t.getCategory() +
                ", date=" + t.getDate() +
                '}';
    }
}
