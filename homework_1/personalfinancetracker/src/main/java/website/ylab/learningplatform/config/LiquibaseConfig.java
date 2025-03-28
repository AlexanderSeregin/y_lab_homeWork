package website.ylab.learningplatform.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration class for Liquibase database migrations.
 */
@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfig {

    private final DataSource dataSource;
    private final LiquibaseProperties properties;

    @Autowired
    public LiquibaseConfig(DataSource dataSource, LiquibaseProperties properties) {
        this.dataSource = dataSource;
        this.properties = properties;
    }

    /**
     * Creates a Liquibase bean that will automatically run migrations on startup
     *
     * @return Liquibase instance
     * @throws Exception if an error occurs during migration
     */
    @Bean
    public Liquibase liquibase() throws Exception {
        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection()));
        
        Liquibase liquibase = new Liquibase(
                properties.getChangeLog(),
                new ClassLoaderResourceAccessor(),
                database);
        
        liquibase.update(new Contexts(properties.getContexts()), new LabelExpression());
        return liquibase;
    }
}
