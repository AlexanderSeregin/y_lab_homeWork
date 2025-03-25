package website.ylab.learningplatform.service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.util.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testcontainers.utility.Base58.randomString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class AuthServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("finance_tracker")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))));
    private final String TEST_NAME = "Test User";
    private final String TEST_PASSWORD = "testPassword";
    private AuthService authService;
    private String TEST_EMAIL = "test@example.com";

    @BeforeAll
    static void startContainer() {
        postgres.start();

        // Configure database connection properties
        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USER", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());

        LiquibaseConfig.getInstance().migrate();
    }

    @BeforeEach
    void setUp() {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
             Statement stmt = conn.createStatement()) {
            // Clean up any existing test data
            stmt.execute("DELETE FROM finance_schema.users WHERE email = '" + TEST_EMAIL + "'");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set up test data", e);
        }
        TEST_EMAIL = "test" + randomString(10) + "+@example.com";
        authService = new AuthService();
    }


    @Test
    void login_WithInvalidEmail_ShouldReturnFalse() {
        // Arrange - Register a user first
        authService.register(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);

        // Act
        boolean result = authService.login("wrong@example.com", PasswordEncoder.encode(TEST_PASSWORD));

        // Assert
        assertFalse(result, "Login with invalid email should fail");
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnFalse() {
        // Arrange - Register a user first
        authService.register(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);

        // Act
        boolean result = authService.login(TEST_EMAIL, PasswordEncoder.encode("wrongPassword"));

        // Assert
        assertFalse(result, "Login with invalid password should fail");
    }
}
