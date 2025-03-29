package website.ylab.learningplatform.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDto testUserDto;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setBalance(new BigDecimal("1000.00"));
        testUser.setAdmin(false);

        // Setup admin user
        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setEmail("admin@example.com");
        adminUser.setBalance(new BigDecimal("2000.00"));
        adminUser.setAdmin(true);

        // Setup user DTO
        testUserDto = new UserDto(1L, "test@example.com", null, new BigDecimal("1000.00"), false);
    }

    @Test
    void getCurrentUserProfile_UserExists_ReturnsUserDto() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<?> response = userController.getCurrentUserProfile(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(UserDto.class, response.getBody());
        UserDto returnedDto = (UserDto) response.getBody();
        assertEquals(testUser.getId(), returnedDto.getId());
        assertEquals(testUser.getEmail(), returnedDto.getEmail());
        assertNull(returnedDto.getPassword());
    }

    @Test
    void getCurrentUserProfile_UserNotFound_ReturnsNotFound() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.getCurrentUserProfile(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("User not found", errorMap.get("error"));
    }

    @Test
    void updateUserProfile_ValidUpdate_ReturnsUpdatedUser() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setEmail("updated@example.com");
        updateDto.setPassword("newPassword");

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<?> response = userController.updateUserProfile(updateDto, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUser(testUser);
        verify(userService).updatePassword(eq(testUser), eq("newPassword"));
        assertEquals("updated@example.com", testUser.getEmail());
    }

    @Test
    void updateUserProfile_UserNotFound_ReturnsNotFound() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setEmail("updated@example.com");

        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.updateUserProfile(updateDto, 999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).updateUser(any());
    }

    @Test
    void getAllUsers_AdminUser_ReturnsAllUsers() {
        // Arrange
        List<User> allUsers = Arrays.asList(testUser, adminUser);
        when(userService.getUserById(2L)).thenReturn(adminUser);
        when(userService.getAllUsers()).thenReturn(allUsers);
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDto(user.getId(), user.getEmail(), null, user.getBalance(), user.getIsAdmin());
        });

        // Act
        ResponseEntity<?> response = userController.getAllUsers(2L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        List<?> returnedUsers = (List<?>) response.getBody();
        assertEquals(2, returnedUsers.size());
    }

    @Test
    void getAllUsers_NonAdminUser_ReturnsForbidden() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = userController.getAllUsers(1L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(userService, never()).getAllUsers();
    }

    @Test
    void getAllUsers_UserNotFound_ReturnsForbidden() {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.getAllUsers(999L);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(userService, never()).getAllUsers();
    }
}
