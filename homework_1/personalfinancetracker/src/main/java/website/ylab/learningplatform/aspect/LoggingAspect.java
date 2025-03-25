package website.ylab.learningplatform.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class.getName());

    @Around("execution(* website.ylab.learningplatform.service.*.*(..)) || execution(* website.ylab.learningplatform.web.servlet.*.*(..))")
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
}
