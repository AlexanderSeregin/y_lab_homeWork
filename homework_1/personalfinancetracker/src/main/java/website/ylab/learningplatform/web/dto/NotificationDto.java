package website.ylab.learningplatform.web.dto;

import java.util.Date;

public class NotificationDto {
    private Long id;

    private Long userId;

    private String message;

    public NotificationDto() {
    }

    public NotificationDto(Long id, Long userId, String message, Date createdAt, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.message = message;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
