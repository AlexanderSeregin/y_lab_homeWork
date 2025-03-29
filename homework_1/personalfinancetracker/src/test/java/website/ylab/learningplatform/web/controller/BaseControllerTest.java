package website.ylab.learningplatform.web.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseControllerTest {

    private TestBaseController baseController;

    @BeforeEach
    void setUp() {
        baseController = new TestBaseController();
    }

    @Test
    void handleValidationExceptions_ReturnsErrorMap() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Field must not be empty");
        violations.add(violation);
        
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getConstraintViolations()).thenReturn(violations);

        // Act
        ResponseEntity<Map<String, String>> response = baseController.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Field must not be empty", response.getBody().get("error"));
    }

    @Test
    void handleValidationExceptions_MultipleViolations_ReturnsJoinedErrorMessages() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        when(violation1.getMessage()).thenReturn("Field must not be empty");
        violations.add(violation1);
        
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        when(violation2.getMessage()).thenReturn("Value must be positive");
        violations.add(violation2);
        
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getConstraintViolations()).thenReturn(violations);

        // Act
        ResponseEntity<Map<String, String>> response = baseController.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        String errorMessage = response.getBody().get("error");
        assertTrue(errorMessage.contains("Field must not be empty"));
        assertTrue(errorMessage.contains("Value must be positive"));
        assertTrue(errorMessage.contains(", "));
    }

    @Test
    void handleExceptions_ReturnsErrorMap() {
        // Arrange
        Exception ex = new RuntimeException("Something went wrong");

        // Act
        ResponseEntity<Map<String, String>> response = baseController.handleExceptions(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Something went wrong", response.getBody().get("error"));
    }

    // Test implementation of BaseController for testing
    private static class TestBaseController extends BaseController {
        // No additional implementation needed
    }
}
