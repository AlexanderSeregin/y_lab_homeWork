package website.ylab.learningplatform.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {
    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* website.ylab.learningplatform.service.*.*(..)) || execution(* website.ylab.learningplatform.web.servlet.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        logger.info(String.format("Executing %s.%s with parameters: %s",
                className, methodName, Arrays.toString(joinPoint.getArgs())));

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info(String.format("%s.%s executed in %d ms",
                    className, methodName, (endTime - startTime)));
            return result;
        } catch (Throwable e) {
            logger.severe(String.format("%s.%s threw exception: %s",
                    className, methodName, e.getMessage()));
            throw e;
        }
    }
}
