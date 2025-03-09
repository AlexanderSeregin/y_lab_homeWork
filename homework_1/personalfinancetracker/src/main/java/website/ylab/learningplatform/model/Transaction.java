package website.ylab.learningplatform.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Transaction {
    private static long counter = 0;

    private long id;
    private long userId;
    private boolean isIncome;
    private String description;
    private BigDecimal amount;
    private Category category;
    private Date date;

    public Long getUserId() {
        return userId;
    }

    public Transaction(long userId, boolean isIncome, String description, BigDecimal amount, Category category, Date date) {
        this.id = counter++;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
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

    public boolean isIncome() {
        return isIncome;
    }

    public Category getCategory() {
        return category;
    }

    private String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setAmount(BigDecimal newAmount) {
        this.amount = newAmount;
    }

    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }
}
