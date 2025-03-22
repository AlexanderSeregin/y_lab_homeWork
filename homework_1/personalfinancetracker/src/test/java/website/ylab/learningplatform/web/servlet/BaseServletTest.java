package website.ylab.learningplatform.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseServletTest {

    private TestBaseServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new TestBaseServlet();
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testReadRequestBody() throws IOException {
        // Arrange
        String jsonBody = "{\"name\":\"Test User\",\"email\":\"test@example.com\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonBody));
        when(request.getReader()).thenReturn(reader);

        // Act
        TestDto dto = servlet.testReadRequestBody(request, TestDto.class);

        // Assert
        assertNotNull(dto);
        assertEquals("Test User", dto.getName());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void testWriteResponse() throws IOException {
        // Arrange
        TestDto dto = new TestDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        // Act
        servlet.testWriteResponse(response, dto);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        // Parse the response to verify it contains the expected data
        String jsonResponse = responseWriter.toString();
        ObjectMapper mapper = new ObjectMapper();
        TestDto resultDto = mapper.readValue(jsonResponse, TestDto.class);

        assertEquals("Test User", resultDto.getName());
        assertEquals("test@example.com", resultDto.getEmail());
    }

    @Test
    void testWriteErrorResponse() throws IOException {
        // Arrange
        int status = HttpServletResponse.SC_BAD_REQUEST;
        String errorMessage = "Invalid input";

        // Act
        servlet.testWriteErrorResponse(response, status, errorMessage);

        // Assert
        verify(response).setStatus(status);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        // Verify the response contains the error message
        String jsonResponse = responseWriter.toString();
        assertTrue(jsonResponse.contains("\"error\":\"Invalid input\""));
    }

    // Helper method to check if a string contains a substring
    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }

    // Test implementation of BaseServlet that exposes protected methods for testing
    private static class TestBaseServlet extends BaseServlet {
        public <T> T testReadRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
            return readRequestBody(request, clazz);
        }

        public void testWriteResponse(HttpServletResponse response, Object object) throws IOException {
            writeResponse(response, object);
        }

        public void testWriteErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
            writeErrorResponse(response, status, message);
        }
    }

    // Test DTO class for JSON serialization/deserialization
    private static class TestDto {
        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
