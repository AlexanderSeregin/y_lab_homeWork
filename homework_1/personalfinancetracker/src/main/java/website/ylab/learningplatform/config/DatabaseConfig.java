package website.ylab.learningplatform.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database configuration class that manages the connection pool using HikariCP
 */
@Repository
@Configuration
public class DatabaseConfig {
    private final HikariDataSource dataSource;

    public DatabaseConfig(
            @Value("${spring.datasource.url}") String jdbcUrl,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name}") String driverClassName,
            @Value("${spring.datasource.hikari.maximum-pool-size}") int maximumPoolSize,
            @Value("${spring.datasource.hikari.auto-commit}") boolean autoCommit,
            @Value("${spring.datasource.hikari.connection-timeout}") long connectionTimeout
    ) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName(driverClassName);
            hikariConfig.setMaximumPoolSize(maximumPoolSize);
            hikariConfig.setAutoCommit(autoCommit);
            hikariConfig.setConnectionTimeout(connectionTimeout);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database config", e);
        }
    }

    /**
     * Get a connection from the connection pool
     *
     * @return database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Get the configured data source
     *
     * @return HikariCP data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Close the data source and release all resources
     */
    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
