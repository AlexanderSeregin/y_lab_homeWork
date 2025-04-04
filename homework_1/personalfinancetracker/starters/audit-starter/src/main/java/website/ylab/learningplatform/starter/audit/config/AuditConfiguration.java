package website.ylab.learningplatform.starter.audit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import website.ylab.learningplatform.starter.audit.aspect.AuditAspect;

/**
 * Configuration for the audit aspect.
 * This will be loaded when the @EnableAudit annotation is used.
 */
@Configuration
@EnableAspectJAutoProxy
public class AuditConfiguration {

    @Value("${audit.aspect.base-package:website.ylab.learningplatform}")
    private String basePackage;

    @Bean
    public AuditAspect auditAspect() {
        return new AuditAspect(basePackage);
    }
}
