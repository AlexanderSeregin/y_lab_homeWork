package website.ylab.learningplatform.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database configuration class that manages the connection pool using HikariCP
 */
public class DatabaseConfig {
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    private final HikariDataSource dataSource;
    private final Config config;

    private DatabaseConfig() {
        try {
            // Load configuration file
            this.config = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))
                    .withFallback(ConfigFactory.load());

            // Configure HikariCP connection pool
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getString("database.url"));
            hikariConfig.setUsername(config.getString("database.username"));
            hikariConfig.setPassword(config.getString("database.password"));
            hikariConfig.setDriverClassName(config.getString("database.driver"));
            hikariConfig.setMaximumPoolSize(config.getInt("database.maximumPoolSize"));
            hikariConfig.setAutoCommit(config.getBoolean("database.autoCommit"));
            hikariConfig.setConnectionTimeout(config.getLong("database.connectionTimeout"));

            // Set additional properties
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database config", e);
        }
    }

    /**
     * Get the singleton instance of DatabaseConfig
     * @return database config instance
     */
    public static DatabaseConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Get a connection from the connection pool
     * @return database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Get the configured data source
     * @return HikariCP data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Get the application configuration
     * @return application config
     */
    public Config getConfig() {
        return config;
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
