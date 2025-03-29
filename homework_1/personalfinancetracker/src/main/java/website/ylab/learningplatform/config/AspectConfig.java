package website.ylab.learningplatform.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import website.ylab.learningplatform.aspect.LoggingAspect;

/**
 * Configuration class for AspectJ aspects.
 * This class initializes and registers all aspects used in the application.
 */
public class AspectConfig {
    private static final Logger logger = LogManager.getLogger(AspectConfig.class.getName());
    private static AspectConfig instance;

    private final LoggingAspect loggingAspect;

    private AspectConfig() {
        loggingAspect = new LoggingAspect();
    }

    public static synchronized AspectConfig getInstance() {
        if (instance == null) {
            instance = new AspectConfig();
        }
        return instance;
    }
}


