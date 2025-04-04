package website.ylab.learningplatform.starter.audit.annotation;

import org.springframework.context.annotation.Import;
import website.ylab.learningplatform.starter.audit.config.AuditConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables audit functionality in the application.
 * Add this annotation to a configuration class to enable transaction auditing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AuditConfiguration.class)
public @interface EnableAudit {
}
