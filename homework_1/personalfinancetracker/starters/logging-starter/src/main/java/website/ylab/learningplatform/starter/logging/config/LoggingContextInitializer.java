package website.ylab.learningplatform.starter.logging.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ApplicationContextInitializer for the logging aspect.
 * This will be automatically loaded by Spring's ApplicationContextInitializer mechanism.
 */
public class LoggingContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // Register the LoggingAutoConfiguration class
        System.out.println("Initializing logging configuration");
    }
}
