package website.ylab.learningplatform.model;

public class Notification {
    private String message;
    private long userId;

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return userId;
    }
}
