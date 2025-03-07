package website.ylab.learningplatform.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Transaction {
    private static long counter = 0;

    private long id;
    private long userId;
    private String description;
    private BigDecimal amount;
    private String category;
    private Calendar date;

    public Long getUserId() {
        return userId;
    }

    public Transaction(long userId, String description, double amount, String category, String date) {
        this.id = counter++;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
