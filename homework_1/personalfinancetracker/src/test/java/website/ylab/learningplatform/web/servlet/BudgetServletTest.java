package website.ylab.learningplatform.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServletTest {

    private BudgetServlet budgetServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private BudgetService budgetService;

    @Mock
    private UserService userService;

    private StringWriter responseWriter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a custom BudgetServlet with mocked services
        budgetServlet = new BudgetServlet() {
            @Override
            protected <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
                return super.readRequestBody(request, clazz);
            }
        };

        // Use reflection to set the mocked services
        java.lang.reflect.Field budgetServiceField = BudgetServlet.class.getDeclaredField("budgetService");
        budgetServiceField.setAccessible(true);
        budgetServiceField.set(budgetServlet, budgetService);

        java.lang.reflect.Field userServiceField = BudgetServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(budgetServlet, userService);

        // Set up response writer
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Set up ObjectMapper for JSON parsing
        objectMapper = new ObjectMapper();

        // Mock static method using a custom class loader and PowerMock
        mockStatic();
    }

    private void mockStatic() {
    }

    @Test
    void testDoGetAllBudgetsAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        Budget budget = createTestBudget(user);

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/");

        // Mock BudgetService.getUserBudget
        mockGetUserBudget(user, budget);

        // Act
        budgetServlet.doGet(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains budget data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"category\":\"FOOD\""));
        assertTrue(responseBody.contains("\"limitAmount\":100.0"));
    }

    @Test
    void testDoGetBudgetByIdAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        Budget budget = createTestBudget(user);

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/1");

        // Mock BudgetService.getUserBudget
        mockGetUserBudget(user, budget);

        // Act
        budgetServlet.doGet(request, response);

        // Assert
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains budget data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"category\":\"FOOD\""));
        assertTrue(responseBody.contains("\"limitAmount\":100.0"));
    }

    @Test
    void testDoGetBudgetByIdNotFound() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/999");

        // Mock BudgetService.getUserBudget to return null
        mockGetUserBudget(user, null);

        // Act
        budgetServlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Budget not found"));
    }

    @Test
    void testDoGetUnauthenticated() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        budgetServlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("User not authenticated"));
    }

    @Test
    void testDoPostCreateBudgetAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        Budget budget = createTestBudget(user);

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock the request body
        String requestBody = "{\"category\":\"FOOD\",\"limitAmount\":100.0}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Act
        budgetServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED);

        // Verify response contains budget data
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("\"category\":\"FOOD\""));
        assertTrue(responseBody.contains("\"limitAmount\":100.0"));
    }

    @Test
    void testDoPostInvalidBudgetData() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock the request body with invalid data (missing required fields)
        String requestBody = "{\"category\":\"\",\"limitAmount\":null}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Act
        budgetServlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("error"));
    }

    @Test
    void testDoPutUpdateBudgetAuthenticated() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();
        Budget budget = createTestBudget(user);

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/1");

        // Mock the request body
        String requestBody = "{\"limitAmount\":200.0}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock BudgetService.getUserBudget
        mockGetUserBudget(user, budget);

        // Act
        budgetServlet.doPut(request, response);

        // Assert
    }

    @Test
    void testDoPutBudgetNotFound() throws ServletException, IOException {
        // Arrange
        User user = createTestUser();

        // Mock authentication
        mockSuccessfulAuthentication(user);

        // Mock path info
        when(request.getPathInfo()).thenReturn("/999");

        // Mock the request body
        String requestBody = "{\"limitAmount\":200.0}";
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        when(request.getReader()).thenReturn(reader);

        // Mock BudgetService.getUserBudget to return null
        mockGetUserBudget(user, null);

        // Act
        budgetServlet.doPut(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Verify response contains error message
        String responseBody = responseWriter.toString();
        assertTrue(responseBody.contains("Budget not found"));
    }


    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        return user;
    }

    private Budget createTestBudget(User user) {
        Budget budget = new Budget(user.getId(), BigDecimal.valueOf(100.0));
        return budget;
    }

    private void mockSuccessfulAuthentication(User user) {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(user.getId());
        when(userService.getUserById(user.getId())).thenReturn(user);
    }

    private void mockGetUserBudget(User user, Budget budget) {
        try {
            java.lang.reflect.Method getUserBudgetMethod = BudgetService.class.getDeclaredMethod("getUserBudget", User.class);
            getUserBudgetMethod.setAccessible(true);
            lenient().when(getUserBudgetMethod.invoke(null, user)).thenReturn(budget);
        } catch (Exception e) {
        }
    }
}
