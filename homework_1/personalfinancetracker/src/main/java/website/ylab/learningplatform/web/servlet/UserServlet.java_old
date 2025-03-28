package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

public class UserServlet extends BaseServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/me")) {
            UserDto userDto = UserMapper.INSTANCE.toDto(user);
            userDto.setPassword(null); // Don't send password back
            writeResponse(response, userDto);
        } else if (pathInfo.equals("/balance")) {
            writeResponse(response, new BalanceResponse(user.getBalance()));
        } else {
            writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/me")) {
            try {
                UserDto userDto = readRequestBody(request, UserDto.class);
                if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
                    user.setEmail(userDto.getEmail());
                }

                if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                    user.setPassword(userDto.getPassword()); // Password will be encoded in the service
                }
                user = userService.updateUser(user);

                // Return updated user
                UserDto responseDto = UserMapper.INSTANCE.toDto(user);
                responseDto.setPassword(null); // Don't send password back
                writeResponse(response, responseDto);
            } catch (Exception e) {
                writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating user: " + e.getMessage());
            }
        } else {
            writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
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

    private static class BalanceResponse {
        private final BigDecimal balance;

        public BalanceResponse(BigDecimal balance) {
            this.balance = balance;
        }

        public BigDecimal getBalance() {
            return balance;
        }
    }
}
