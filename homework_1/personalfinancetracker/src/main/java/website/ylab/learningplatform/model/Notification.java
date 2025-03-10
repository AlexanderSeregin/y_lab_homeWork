package website.ylab.learningplatform.model;

public class Notification {
    private final String message;
    private long userId;

    public Notification(String message) {
        this.message = message;
    }

    public Notification(long userId, String message) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return userId;
    }
}
