package website.ylab.learningplatform.config;

import java.util.logging.Logger;

/**
 * Configuration class for AspectJ aspects.
 * This class initializes and registers all aspects used in the application.
 */
public class AspectConfig {
    private static final Logger logger = Logger.getLogger(AspectConfig.class.getName());
    private static AspectConfig instance;

    private AspectConfig() {
        // No direct initialization needed when using load-time weaving
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
        logger.info("AspectJ aspects initialized via load-time weaving");
    }
}
