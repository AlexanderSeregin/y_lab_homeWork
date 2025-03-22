package website.ylab.learningplatform.service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import website.ylab.learningplatform.config.LiquibaseConfig;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

import java.math.BigDecimal;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.testcontainers.utility.Base58.randomString;


@Testcontainers
class AdminServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("finance_tracker")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))));
    private final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private User testUser;

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
        if (testUser != null && testUser.getId() != null) {
            try (Connection conn = DriverManager.getConnection(
                    postgres.getJdbcUrl(),
                    postgres.getUsername(),
                    postgres.getPassword());
                 Statement stmt = conn.createStatement()) {
                // Clean up any existing test data
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM finance_schema.users WHERE id = ?");
                pstmt.setInt(1, testUser.getId().intValue());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to set up test data", e);
            }
        }

        // Initialize test user object
        testUser = new User();
        testUser.setEmail("test" + randomString(5) + "@example.com");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        testUser.setBalance(INITIAL_BALANCE);
        testUser.setBlocked(false);

        // Save the test user to the database
        PostgresUserRepository.getInstance().save(testUser);
    }

    @Test
    void blockUser_WithNonExistentUser_ShouldNotThrowException() {
        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> AdminService.blockUser(999L), "Should not throw exception for non-existent user");
    }
}
