package website.ylab.learningplatform.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Controller", description = "Endpoints for managing user notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(summary = "Get user notifications", description = "Retrieves notifications for the authenticated user")
    public ResponseEntity<?> getUserNotifications(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User user = (User) session.getAttribute("user");
        Notification notification = notificationService.getNotification(user.getId());
        
        if (notification == null) {
            return ResponseEntity.ok().body("No notifications");
        }
        
        return ResponseEntity.ok(notification);
    }

    @PostMapping
    @Operation(summary = "Create notification", description = "Creates a new notification for a user (admin only)")
    public ResponseEntity<?> createNotification(
            @RequestParam Long userId,
            @RequestParam String message,
            HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User currentUser = (User) session.getAttribute("user");
        if (!currentUser.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can create notifications");
        }

        User targetUser = new User();
        targetUser.setId(userId);
        
        notificationService.sendNotification(targetUser, message);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Notification sent");
    }
}
