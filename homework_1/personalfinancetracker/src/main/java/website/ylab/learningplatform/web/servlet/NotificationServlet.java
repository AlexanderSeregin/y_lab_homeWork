package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.NotificationService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.NotificationDto;
import website.ylab.learningplatform.web.mapper.NotificationMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class NotificationServlet extends BaseServlet {
    private final NotificationService notificationService = new NotificationService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all notifications for user
            Notification notification = NotificationService.getNotification(user);
            NotificationDto notificationDto = NotificationMapper.INSTANCE.toDto(notification);
            writeResponse(response, notificationDto);
        } else {
            try {
                // Get notification by ID
                long notificationId = Long.parseLong(pathInfo.substring(1));
                Notification notification = NotificationService.getNotification(user);

                if (notification != null) {
                    writeResponse(response, NotificationMapper.INSTANCE.toDto(notification));
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Notification not found");
                }
            } catch (NumberFormatException e) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid notification ID");
            }
        }
    }


    private User authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return null;
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);

        if (user == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return null;
        }

        // Set user email for audit
        request.setAttribute("userEmail", user.getEmail());

        return user;
    }
}
