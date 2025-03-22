package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.GoalService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.GoalDto;
import website.ylab.learningplatform.web.mapper.GoalMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class GoalServlet extends BaseServlet {
    private final GoalService goalService = new GoalService();
    private final UserService userService = new UserService();
    private final Validator validator;

    public GoalServlet() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all goals for user
            Goal goal = GoalService.getUserGoal(user);
            GoalDto goalDto = GoalMapper.INSTANCE.toDto(goal);
            writeResponse(response, goalDto);
        } else {
            try {
                Goal goal = GoalService.getUserGoal(user);
                Goal found = goal;

                if (found != null) {
                    writeResponse(response, GoalMapper.INSTANCE.toDto(found));
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Goal not found");
                }
            } catch (NumberFormatException e) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid goal ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        try {
            GoalDto goalDto = readRequestBody(request, GoalDto.class);

            // Validate goal input
            Set<ConstraintViolation<GoalDto>> violations = validator.validate(goalDto);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
                return;
            }

            // Set the user ID
            goalDto.setUserId(user.getId());

            // Create goal
            Goal goal = GoalMapper.INSTANCE.toEntity(goalDto);
            goal = GoalService.setGoal(user, goal.getAmount());

            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, GoalMapper.INSTANCE.toDto(goal));
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating goal: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

//        String pathInfo = request.getPathInfo();
//        if (pathInfo == null || pathInfo.equals("/")) {
//            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Goal ID is required");
//            return;
//        }

        try {
            GoalDto goalDto = readRequestBody(request, GoalDto.class);

            // Check if goal exists and belongs to user
            Goal goal = GoalService.getUserGoal(user);
            Goal found = goal;

            if (found == null) {
                writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Goal not found or does not belong to user");
                return;
            }
            //FIXME!
            //Update goal

            if (goalDto.getAmount() != null) {
                found.setAmount(goalDto.getAmount());
            }

            System.out.println(found.getId() + " " + found.getUserId() + " " + found.getAmount());
            Goal updated = GoalService.setGoal(found);
            writeResponse(response, GoalMapper.INSTANCE.toDto(updated));
        } catch (NumberFormatException e) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid goal ID");
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating goal: " + e.getMessage());
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
