package team.three.usedstroller.api.common.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@Aspect
@AllArgsConstructor
public class ProductLogAop {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Around("execution(public * team.three.usedstroller.api.product.controller..*(..)) "
  )
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("================================== API CALL ==========================================");
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    log.info("### IP : " + (request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR")));
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    String baseUrl = joinPoint.getTarget().getClass().isAnnotationPresent(RequestMapping.class)
        ? String.join(", ", joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value())
        : "";
    String endpointUrl = method.isAnnotationPresent(PostMapping.class) ? String.join(", ", method.getAnnotation(PostMapping.class).value())
        : method.isAnnotationPresent(GetMapping.class) ? String.join(", ", method.getAnnotation(GetMapping.class).value())
            : method.isAnnotationPresent(PutMapping.class) ? String.join(", ", method.getAnnotation(PutMapping.class).value())
                : method.isAnnotationPresent(DeleteMapping.class) ? String.join(", ", method.getAnnotation(DeleteMapping.class).value())
                    : null;
    String httpMethod = method.isAnnotationPresent(PostMapping.class) ? "POST"
        : method.isAnnotationPresent(GetMapping.class) ? "GET"
            : method.isAnnotationPresent(PutMapping.class) ? "PUT"
                : method.isAnnotationPresent(DeleteMapping.class) ? "DELETE"
                    : null;
    log.info("### METHOD : " + httpMethod);
    log.info("### AUTH : " + request.getHeader("Authorization"));
    log.info("### URL : " + baseUrl + (endpointUrl != null ? (baseUrl.endsWith("/") ? endpointUrl.startsWith("/") ? endpointUrl.substring(1) : endpointUrl : endpointUrl) : ""));

    Object result;
    try {
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // null 값 제외

      String[] paramNames = methodSignature.getParameterNames();
      for (int i = 0; i < joinPoint.getArgs().length; i++) {
        Object arg = joinPoint.getArgs()[i];
        String prefix = (paramNames[i].equals("dto") || paramNames[i].equals("reqDto")) ? "" : paramNames[i] + "=";
        try {
          String serializedArg = objectMapper.writeValueAsString(arg);
          log.info("### REQ : " + prefix + serializedArg);
        } catch (JsonProcessingException e) {
          //log.error("Error serializing argument: " + arg, e);
        }
      }
      result = joinPoint.proceed();
      String responseBody = objectMapper.writeValueAsString(result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getBody() : result);
      log.info("### RES : " + responseBody);
    } catch (Throwable e) {
      throw e;
    }
    log.info("======================================================================================");
    return result;
  }
}
