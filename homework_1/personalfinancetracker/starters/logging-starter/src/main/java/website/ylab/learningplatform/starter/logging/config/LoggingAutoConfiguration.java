package website.ylab.learningplatform.starter.logging.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import website.ylab.learningplatform.starter.logging.aspect.LoggingAspect;

/**
 * Auto-configuration for the logging aspect.
 * This will be automatically loaded by Spring's auto-configuration mechanism.
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    @Value("${logging.aspect.base-package:website.ylab.learningplatform}")
    private String basePackage;

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect(basePackage);
    }
}
