package website.ylab.learningplatform.config;

import com.typesafe.config.Config;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

/**
 * Liquibase configuration class for handling database migrations
 */
public class LiquibaseConfig {
    private static final LiquibaseConfig INSTANCE = new LiquibaseConfig();
    private final DatabaseConfig dbConfig;

    private LiquibaseConfig() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    /**
     * Get the singleton instance of LiquibaseConfig
     *
     * @return liquibase config instance
     */
    public static LiquibaseConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Execute Liquibase migrations
     */
    public void migrate() {
        Config config = dbConfig.getConfig();
        String changeLogPath = config.getString("liquibase.changeLog");
        String contexts = config.getString("liquibase.contexts");

        try (Connection connection = dbConfig.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    changeLogPath,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update((contexts == null || contexts.isEmpty()) ? new Contexts() : new Contexts(contexts), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException("Liquibase migration failed", e);
        }
    }
}
