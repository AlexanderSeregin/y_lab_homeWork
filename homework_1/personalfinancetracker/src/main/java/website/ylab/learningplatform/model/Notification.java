package website.ylab.learningplatform.model;

public class Notification {
    private final String message;
    private long id;
    private long userId;

    public Notification(String message) {
        this.message = message;
    }

    public Notification(long userId, String message) {
        this.message = message;
        this.userId = userId;
    }

    public Notification(long id, String message, long userId) {
        this.id = id;
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long notificationId) {
        this.id = notificationId;
    }

    public Object getUserId() {
        return userId;
    }
}
