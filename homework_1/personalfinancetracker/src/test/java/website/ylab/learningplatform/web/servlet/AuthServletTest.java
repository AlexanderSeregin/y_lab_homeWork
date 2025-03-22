package website.ylab.learningplatform.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServletTest {

    private AuthServlet authServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private AuthService authService;

    private StringWriter responseWriter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a custom AuthServlet with mocked AuthService
        authServlet = new AuthServlet() {
            @Override
            protected <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
                return super.readRequestBody(request, clazz);
            }
        };

        // Use reflection to set the mocked AuthService
        java.lang.reflect.Field authServiceField = AuthServlet.class.getDeclaredField("authService");
        authServiceField.setAccessible(true);
        authServiceField.set(authServlet, authService);

        // Set up response writer
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Set up ObjectMapper for JSON parsing
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDoPostWithInvalidPath() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn(null);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        assertTrue(responseWriter.toString().contains("Invalid endpoint"));
    }

    @Test
    void testDoPostWithInvalidEndpoint() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/invalid");

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        assertTrue(responseWriter.toString().contains("Endpoint not found"));
    }

    @Test
    void testDoLoginSuccess() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/login");

        // Mock the request body
        String requestBody = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock the user
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        // Mock the auth service
        when(authService.loginUser("test@example.com", "password123")).thenReturn(user);

        // Mock the session
        when(request.getSession(true)).thenReturn(session);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(session).setAttribute("userId", 1L);
        verify(session).setAttribute("userEmail", "test@example.com");
        verify(request).setAttribute("userEmail", "test@example.com");
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Verify response contains user data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("test@example.com"));
        assertTrue(responseBody.contains("Test User"));
    }

    @Test
    void testDoLoginFailure() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/login");

        // Mock the request body
        String requestBody = "{\"email\":\"test@example.com\",\"password\":\"wrongpassword\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock the auth service to return null (login failure)
        when(authService.loginUser("test@example.com", "wrongpassword")).thenReturn(null);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(session, never()).setAttribute(eq("userId"), any());
        verify(session, never()).setAttribute(eq("userEmail"), any());

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Invalid email or password"));
    }

    @Test
    void testDoRegisterSuccess() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/register");

        // Mock the request body
        String requestBody = "{\"name\":\"New User\",\"email\":\"new@example.com\",\"password\":\"password123\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock the auth service
        when(authService.isEmailRegistered("new@example.com")).thenReturn(false);
        when(authService.register("New User", "new@example.com", "password123")).thenReturn(true);

        // Mock the session
        when(request.getSession(true)).thenReturn(session);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(session).setAttribute(eq("userId"), any());
        verify(session).setAttribute(eq("userEmail"), eq("new@example.com"));
        verify(request).setAttribute("userEmail", "new@example.com");

        // Verify response contains user data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("new@example.com"));
        assertTrue(responseBody.contains("New User"));
    }

    @Test
    void testDoRegisterEmailAlreadyExists() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/register");

        // Mock the request body
        String requestBody = "{\"name\":\"Existing User\",\"email\":\"existing@example.com\",\"password\":\"password123\"}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock the auth service to indicate email already registered
        when(authService.isEmailRegistered("existing@example.com")).thenReturn(true);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
        verify(authService, never()).register(anyString(), anyString(), anyString());
        verify(session, never()).setAttribute(eq("userId"), any());
        verify(session, never()).setAttribute(eq("userEmail"), any());

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Email already registered"));
    }

    @Test
    void testDoLogout() throws ServletException, IOException {
        // Arrange
        when(request.getPathInfo()).thenReturn("/logout");
        when(request.getSession(false)).thenReturn(session);

        // Act
        authServlet.doPost(request, response);

        // Assert
        verify(session).invalidate();
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
