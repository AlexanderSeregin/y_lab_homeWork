package website.ylab.learningplatform.starter.logging.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Aspect for logging method execution in services and controllers.
 */
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    private String basePackage;
    
    public LoggingAspect(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Logs method execution time and parameters for service and controller methods
     *
     * @param joinPoint the join point for the intercepted method
     * @return the result of the method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("execution(* website.ylab.learningplatform..service.*.*(..)) || execution(* website.ylab.learningplatform..web.controller.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        logger.info("Executing {}.{} with parameters: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("{}.{} executed in {} ms", className, methodName, (endTime - startTime));
            return result;
        } catch (Throwable e) {
            logger.error("{}.{} threw exception: {}", className, methodName, e.getMessage());
            throw e;
        }
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public static String getBasePackagePattern() {
        return "website.ylab.learningplatform";
    }
}
