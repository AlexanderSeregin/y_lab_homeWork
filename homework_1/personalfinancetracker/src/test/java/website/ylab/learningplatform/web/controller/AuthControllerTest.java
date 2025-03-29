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
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.web.dto.UserDto;
import website.ylab.learningplatform.web.mapper.UserMapper;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setBalance(new BigDecimal("1000.00"));
        testUser.setAdmin(false);

        // Setup user DTO
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");
        testUserDto.setPassword("password123");
        testUserDto.setBalance(new BigDecimal("1000.00"));
        testUserDto.setAdmin(false);
    }

    @Test
    void login_ValidCredentials_ReturnsUserWithAuthToken() {
        // Arrange
        when(authService.loginUser("test@example.com", "password123")).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<?> response = authController.login(testUserDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getHeaders().getFirst("X-Auth-Token"));
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserDto);
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        when(authService.loginUser("test@example.com", "password123")).thenReturn(null);

        // Act
        ResponseEntity<?> response = authController.login(testUserDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("Invalid email or password", errorMap.get("error"));
    }

    @Test
    void login_MissingEmail_ReturnsBadRequest() {
        // Arrange
        UserDto incompleteDto = new UserDto();
        incompleteDto.setPassword("password123");

        // Act
        ResponseEntity<?> response = authController.login(incompleteDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(authService, never()).loginUser(anyString(), anyString());
    }

    @Test
    void login_MissingPassword_ReturnsBadRequest() {
        // Arrange
        UserDto incompleteDto = new UserDto();
        incompleteDto.setEmail("test@example.com");

        // Act
        ResponseEntity<?> response = authController.login(incompleteDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(authService, never()).loginUser(anyString(), anyString());
    }

    @Test
    void login_ServiceException_ReturnsInternalServerError() {
        // Arrange
        when(authService.loginUser(anyString(), anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authController.login(testUserDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertTrue(errorMap.get("error").contains("Error during login"));
    }

    @Test
    void register_NewUser_ReturnsCreatedUser() {
        // Arrange
        when(authService.isEmailRegistered("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(testUserDto)).thenReturn(testUser);
        when(authService.register(testUser.getName(), testUser.getEmail(), testUser.getPassword())).thenReturn(true);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act
        ResponseEntity<?> response = authController.register(testUserDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("1", response.getHeaders().getFirst("X-Auth-Token"));
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof UserDto);
        UserDto returnedDto = (UserDto) response.getBody();
        assertNull(returnedDto.getPassword()); // Password should be cleared in response
    }

    @Test
    void register_ExistingEmail_ReturnsConflict() {
        // Arrange
        when(authService.isEmailRegistered("test@example.com")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.register(testUserDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("Email already registered", errorMap.get("error"));
        verify(authService, never()).register(anyString(), anyString(), anyString());
    }

    @Test
    void register_RegistrationFails_ReturnsInternalServerError() {
        // Arrange
        when(authService.isEmailRegistered("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(testUserDto)).thenReturn(testUser);
        when(authService.register(testUser.getName(), testUser.getEmail(), testUser.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.register(testUserDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("Error during registration", errorMap.get("error"));
    }

    @Test
    void register_ServiceException_ReturnsInternalServerError() {
        // Arrange
        when(authService.isEmailRegistered("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(testUserDto)).thenReturn(testUser);
        when(authService.register(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = authController.register(testUserDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertTrue(errorMap.get("error").contains("Error during registration"));
    }
}
