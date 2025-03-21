package website.ylab.learningplatform.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
public class AuditAspect {
    private static final Logger logger = Logger.getLogger(AuditAspect.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @AfterReturning("execution(* website.ylab.learningplatform.web.servlet.*.do*(..)) && args(request,..)")
    public void auditUserAction(JoinPoint joinPoint, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        String userEmail = getUserEmail(request);
        String ipAddress = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        String httpMethod = request.getMethod();
        
        logger.info(String.format("[AUDIT] Time: %s | User: %s | IP: %s | Action: %s | Method: %s | Endpoint: %s", 
                LocalDateTime.now().format(formatter),
                userEmail,
                ipAddress,
                methodName,
                httpMethod,
                endpoint));
    }
    
    private String getUserEmail(HttpServletRequest request) {
        // In a real application, you would get this from the session or authentication token
        // For simplicity, we'll use a header or attribute if available
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            userEmail = request.getHeader("X-User-Email");
        }
        return userEmail != null ? userEmail : "anonymous";
    }
}
