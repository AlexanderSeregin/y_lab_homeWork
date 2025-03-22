package website.ylab.learningplatform.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import website.ylab.learningplatform.aspect.AuditAspect;
import website.ylab.learningplatform.aspect.LoggingAspect;

/**
 * Configuration class for AspectJ aspects.
 * This class initializes and registers all aspects used in the application.
 */
public class AspectConfig {
    private static final Logger logger = LogManager.getLogger(AspectConfig.class.getName());
    private static AspectConfig instance;
    
    private final AuditAspect auditAspect;
    private final LoggingAspect loggingAspect;

    private AspectConfig() {
        auditAspect = new AuditAspect();
        loggingAspect = new LoggingAspect();
    }

    public static synchronized AspectConfig getInstance() {
        if (instance == null) {
            instance = new AspectConfig();
        }
        return instance;
    }

    /**
     * Initialize all aspects. This method should be called at application startup.
     */
    public void initializeAspects() {
        logger.info("Initializing AspectJ aspects");
        logger.info("AuditAspect and LoggingAspect initialized");
    }
    
    public AuditAspect getAuditAspect() {
        return auditAspect;
    }
    
    public LoggingAspect getLoggingAspect() {
        return loggingAspect;
    }
}
