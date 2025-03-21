package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.util.PasswordEncoder;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;

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

public class AuthServlet extends BaseServlet {
    private final AuthService authService = new AuthService();
    private final Validator validator;

    public AuthServlet() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        
        if (path == null || path.equals("/")) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            return;
        }
        
        switch (path) {
            case "/login":
                doLogin(request, response);
                break;
            case "/register":
                doRegister(request, response);
                break;
            case "/logout":
                doLogout(request, response);
                break;
            default:
                writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserDto userDto = readRequestBody(request, UserDto.class);
            
            // Simple validation for login
            if (userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
                userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Email and password are required");
                return;
            }
            User user = authService.loginUser(userDto.getEmail(), userDto.getPassword());
            if (user != null) {
                // Set user in session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());
                
                // Set user email for audit
                //FIXME!request.setAttribute("userEmail", user.getEmail());
                
                // Return user data (without password)
                UserDto responseDto = UserMapper.INSTANCE.toDto(user);
                responseDto.setPassword(null); // Don't send password back
                writeResponse(response, responseDto);
            } else {
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
            }
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during login: " + e.getMessage());
        }
    }

    /**
     * Handle a registration request.
     *
     * This method is responsible for validating the input, checking if the user
     * already exists, creating a new user and setting the user in the session.
     *
     * @param request The {@link HttpServletRequest} containing the request
     *                parameters.
     * @param response The {@link HttpServletResponse} to send the response to.
     * @throws IOException If an IO error occurs.
     */
    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserDto userDto = readRequestBody(request, UserDto.class);
            
            // Validate user input
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
                return;
            }
            
            // Check if user already exists
            if (authService.isEmailRegistered(userDto.getEmail())) {
                writeErrorResponse(response, HttpServletResponse.SC_CONFLICT, "Email already registered");
                return;
            }
            
            // Create user
            User user = UserMapper.INSTANCE.toEntity(userDto);
            boolean sucess = authService.register(user.getName(), user.getEmail(), user.getPassword());
            
            // Set user in session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            
            // Set user email for audit
            request.setAttribute("userEmail", user.getEmail());
            if (!sucess) {
                writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during registration");
                return;
            }
            // Return user data (without password)
            UserDto responseDto = UserMapper.INSTANCE.toDto(user);
            responseDto.setPassword(null); // Don't send password back
            response.setStatus(HttpServletResponse.SC_CREATED);
            writeResponse(response, responseDto);
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during registration: " + e.getMessage());
        }
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
