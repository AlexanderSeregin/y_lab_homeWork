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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("finance_tracker")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))))
            ;

    private User testUser;
    private final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");

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
            stmt.execute("DELETE FROM finance_schema.users WHERE email = 'test@example.com'");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to set up test data", e);
        }

        // Initialize test user object
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("testPassword");
        testUser.setBalance(INITIAL_BALANCE);
        
        // Save the test user to the database
        PostgresUserRepository.getInstance().save(testUser);
    }

    @Test
    void userChangeEmail_ShouldUpdateEmail() {
        // Arrange
        String newEmail = "updated@example.com";
        
        // Act
        UserService.userChangeEmail(testUser, newEmail);
        
        // Assert
        Optional<User> updatedUser = PostgresUserRepository.getInstance().findById(testUser.getId());
        assertTrue(updatedUser.isPresent(), "User should exist after email update");
        assertEquals(newEmail, updatedUser.get().getEmail(), "Email should be updated");
    }

    @Test
    void userChangePassword_ShouldUpdatePassword() {
        // Arrange
        String newPassword = "newSecurePassword";
        
        // Act
        UserService.userChangePassword(testUser, newPassword);
        
        // Assert
        Optional<User> updatedUser = PostgresUserRepository.getInstance().findById(testUser.getId());
        assertTrue(updatedUser.isPresent(), "User should exist after password update");
        assertEquals(newPassword, updatedUser.get().getPasswordHash(), "Password should be updated");
    }
    
    @Test
    void userDelete_ShouldRemoveUserFromDatabase() {
        // Act
        UserService.userDelete(testUser);
        
        // Assert
        Optional<User> deletedUser = PostgresUserRepository.getInstance().findById(testUser.getId());
        assertFalse(deletedUser.isPresent(), "User should be deleted from the database");
    }
}
