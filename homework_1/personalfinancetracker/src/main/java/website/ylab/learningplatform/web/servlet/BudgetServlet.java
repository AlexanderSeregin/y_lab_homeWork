package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.BudgetDto;
import website.ylab.learningplatform.web.mapper.BudgetMapper;

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

public class BudgetServlet extends BaseServlet {
    private final BudgetService budgetService = new BudgetService();
    private final UserService userService = new UserService();
    private final Validator validator;

    public BudgetServlet() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            Budget budget = BudgetService.getUserBudget(user);
            BudgetDto budgetDto = BudgetMapper.INSTANCE.toDto(budget);
            writeResponse(response, budgetDto);
        } else {
            try {
                long budgetId = Long.parseLong(pathInfo.substring(1));
                Budget budget = BudgetService.getUserBudget(user);
                Budget found = budget;

                if (found != null) {
                    writeResponse(response, BudgetMapper.INSTANCE.toDto(found));
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Budget not found");
                }
            } catch (NumberFormatException e) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid budget ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        try {
            BudgetDto budgetDto = readRequestBody(request, BudgetDto.class);
            Set<ConstraintViolation<BudgetDto>> violations = validator.validate(budgetDto);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
                return;
            }
            budgetDto.setUserId(user.getId());
            Budget budget = BudgetMapper.INSTANCE.toEntity(budgetDto);
            budget = new Budget(user.getId(), budget.getAmount());

            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, BudgetMapper.INSTANCE.toDto(budget));
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating budget: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        try {
            BudgetDto budgetDto = readRequestBody(request, BudgetDto.class);

            Budget budget = BudgetService.getUserBudget(user);
            Budget found = budget;

            if (found == null) {
                writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Budget not found or does not belong to user");
                return;
            }

            if (budgetDto.getamount() != null) {
                found.setAmount(budgetDto.getamount());
            }

            Budget updated = BudgetService.updateBudget(found);
            writeResponse(response, BudgetMapper.INSTANCE.toDto(updated));
        } catch (NumberFormatException e) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid budget ID");
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating budget: " + e.getMessage());
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
        request.setAttribute("userEmail", user.getEmail());
        return user;
    }
}
