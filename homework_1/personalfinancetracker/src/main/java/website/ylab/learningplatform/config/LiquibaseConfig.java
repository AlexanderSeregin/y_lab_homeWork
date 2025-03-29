package website.ylab.learningplatform.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * Liquibase configuration class for handling database migrations
 */
@Component
@Configuration
public class LiquibaseConfig implements InitializingBean {
    private final DatabaseConfig dbConfig;
    private final String changeLogPath;
    private final String contexts;

    @Autowired
    public LiquibaseConfig(
            DatabaseConfig dbConfig,
            @Value("${spring.liquibase.change-log}") String changeLogPath,
            @Value("${spring.liquibase.contexts}") String contexts
    ) {
        this.dbConfig = dbConfig;
        this.changeLogPath = changeLogPath.replace("classpath:", "");
        this.contexts = contexts;
    }

    /**
     * Initialize and run Liquibase migrations after bean properties are set
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        migrate();
    }

    /**
     * Execute Liquibase migrations
     */
    private void migrate() {
        try (Connection connection = dbConfig.getConnection();
             ClassLoaderResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor()) {

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (Liquibase liquibase = new Liquibase(
                    changeLogPath,
                    resourceAccessor,
                    database)) {

                liquibase.update((contexts == null || contexts.isEmpty()) ? new Contexts() : new Contexts(contexts), new LabelExpression());
            }
        } catch (Exception e) {
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}
