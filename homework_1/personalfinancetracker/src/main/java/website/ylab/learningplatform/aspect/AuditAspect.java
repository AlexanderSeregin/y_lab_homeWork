package website.ylab.learningplatform.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
public class AuditAspect {
    private static final Logger logger = LogManager.getLogger(AuditAspect.class.getName());
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @AfterReturning("execution(* website.ylab.learningplatform.web.servlet.*.*(..)) && args(request,..)")
    public void auditUserAction(JoinPoint joinPoint, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        String userEmail = getUserEmail(request);
        String ipAddress = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        String httpMethod = request.getMethod();

        logger.info("[AUDIT] Time: {} | User: {} | IP: {} | Action: {} | Method: {} | Endpoint: {}", LocalDateTime.now().format(formatter), userEmail, ipAddress, methodName, httpMethod, endpoint);
    }

    private String getUserEmail(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            userEmail = request.getHeader("X-User-Email");
        }
        return userEmail != null ? userEmail : "anonymous";
    }
}
