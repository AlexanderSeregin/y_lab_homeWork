package website.ylab.learningplatform.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServletTest {

    private UserServlet userServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private UserService userService;

    private StringWriter responseWriter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a custom UserServlet with mocked services
        userServlet = new UserServlet() {
            @Override
            protected <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
                return super.readRequestBody(request, clazz);
            }
        };

        // Use reflection to set the mocked services
        java.lang.reflect.Field userServiceField = UserServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userServlet, userService);

        // Set up response writer
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Set up ObjectMapper for JSON parsing
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDoGetCurrentUserAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/me");

        // Act
        userServlet.doGet(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains user data but not password
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"email\":\"test@example.com\""));
        assertTrue(responseBody.contains("\"name\":\"Test User\""));
        assertTrue(!responseBody.contains("password"));
    }

    @Test
    void testDoGetUserBalanceAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        user.setBalance(BigDecimal.valueOf(1000.0));

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/balance");

        // Act
        userServlet.doGet(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains balance data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"balance\":1000.0"));
    }

    @Test
    void testDoGetInvalidEndpoint() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/invalid");

        // Act
        userServlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Endpoint not found"));
    }

    @Test
    void testDoGetUnauthenticated() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        userServlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("User not authenticated"));
    }

    @Test
    void testDoPutUpdateUserAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        User updatedUser = createTestUser();
        updatedUser.setEmail("updated@example.com");

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/me");

        // Mock the request body
        String requestBody = "{\"email\":\"updated@example.com\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock userService.updateUser
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        // Act
        userServlet.doPut(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(userService).updateUser(any(User.class));

        // Verify response contains updated user data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"email\":\"updated@example.com\""));
    }

    @Test
    void testDoPutUpdatePasswordAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        User updatedUser = createTestUser();
        updatedUser.setPassword("newpassword"); // In reality, this would be encoded

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/me");

        // Mock the request body
        String requestBody = "{\"password\":\"newpassword\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock userService.updateUser
        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        // Act
        userServlet.doPut(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(userService).updateUser(any(User.class));

        // Verify response does not contain password
        String responseBody = responseWriter.toString();
        assertTrue(!responseBody.contains("password"));
    }

    @Test
    void testDoPutInvalidEndpoint() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/invalid");

        // Act
        userServlet.doPut(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(userService, never()).updateUser(any(User.class));

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Endpoint not found"));
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("password123"); // In reality, this would be encoded
        user.setBalance(BigDecimal.ZERO);
        return user;
    }

    private void mockSuccessfulAuthentication(User user) {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(user.getId());
        when(userService.getUserById(user.getId())).thenReturn(user);
    }
}
