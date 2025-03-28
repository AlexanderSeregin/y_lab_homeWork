package website.ylab.learningplatform.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;


import javax.validation.Valid;
import java.util.Map;

/**
 * REST controller for authentication operations
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API for login, registration, and logout")
public class AuthController extends BaseController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    /**
     * Login a user
     *
     * @param userDto user credentials
     * @param request HTTP request
     * @return user information
     */
    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Authenticate a user with email and password")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() ||
                userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }
        
        try {
            User user = authService.loginUser(userDto.getEmail(), userDto.getPassword());
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());

                UserDto responseDto = userMapper.toDto(user);
                responseDto.setPassword(null); // Don't send password back
                return ResponseEntity.ok(responseDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error during login: " + e.getMessage()));
        }
    }

    /**
     * Register a new user
     *
     * @param userDto user information
     * @param request HTTP request
     * @return created user information
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        try {
            if (authService.isEmailRegistered(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already registered"));
            }

            User user = userMapper.toEntity(userDto);
            boolean success = authService.register(user.getName(), user.getEmail(), user.getPassword());

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());

            request.setAttribute("userEmail", user.getEmail());
            if (!success) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error during registration"));
            }
            
            UserDto responseDto = userMapper.toDto(user);
            responseDto.setPassword(null); // Don't send password back
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Logout a user
     *
     * @param request HTTP request
     * @return empty response
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout a user", description = "Invalidate the user's session")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }
}
