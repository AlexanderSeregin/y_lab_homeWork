package website.ylab.learningplatform.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for user operations
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
public class UserController extends BaseController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Get current user profile
     *
     * @param userId the ID of the authenticated user
     * @return user information
     */
    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "Retrieves the profile of the currently logged in user")
    public ResponseEntity<?> getCurrentUserProfile(@RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        UserDto userDto = userMapper.toDto(user);
        userDto.setPassword(null);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Update user profile
     *
     * @param userDto updated user information
     * @param userId  the ID of the authenticated user
     * @return updated user information
     */
    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Updates the profile of the currently logged in user")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserDto userDto,
                                               @RequestHeader("X-Auth-Token") Long userId) {
        User existingUser = userService.getUserById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userService.updatePassword(existingUser, userDto.getPassword());
        }

        userService.updateUser(existingUser);

        UserDto responseDto = userMapper.toDto(existingUser);
        responseDto.setPassword(null);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get all users (admin only)
     *
     * @param userId the ID of the authenticated user
     * @return list of users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users (admin only)")
    public ResponseEntity<?> getAllUsers(@RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null || !user.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied"));
        }

        List<UserDto> users = StreamSupport.stream(userService.getAllUsers().spliterator(), false)
                .map(u -> {
                    UserDto dto = userMapper.toDto(u);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}
