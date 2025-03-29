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
import website.ylab.learningplatform.service.UserService;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Controller", description = "Endpoints for managing user notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get user notifications", description = "Retrieves notifications for the authenticated user")
    public ResponseEntity<?> getUserNotifications(@RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Notification notification = notificationService.getNotification(user.getId());

        if (notification == null) {
            return ResponseEntity.ok().body("No notifications");
        }

        return ResponseEntity.ok(notification);
    }

    @PostMapping
    @Operation(summary = "Create notification", description = "Creates a new notification for a user (admin only)")
    public ResponseEntity<?> createNotification(
            @RequestParam Long targetUserId,
            @RequestParam String message,
            @RequestHeader("X-Auth-Token") Long userId) {

        User currentUser = userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!currentUser.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can create notifications");
        }

        User targetUser = userService.getUserById(targetUserId);
        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found");
        }

        notificationService.sendNotification(targetUser, message);

        return ResponseEntity.status(HttpStatus.CREATED).body("Notification sent");
    }
}
