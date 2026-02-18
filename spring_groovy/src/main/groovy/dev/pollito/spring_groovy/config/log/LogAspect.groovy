package dev.pollito.spring_groovy.config.log

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
@Slf4j
@CompileStatic
class LogAspect {

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  void controllerPublicMethodsPointcut() {
    // Method is empty as this is just a Pointcut declaration
  }

  @Before("controllerPublicMethodsPointcut()")
  void logBefore(JoinPoint joinPoint) {
    log.info "[${joinPoint.signature.toShortString()}] Args: ${joinPoint.args*.toString().join(', ')}"
  }

  @AfterReturning(pointcut = "controllerPublicMethodsPointcut()", returning = "result")
  void logAfterReturning(JoinPoint joinPoint, Object result) {
    log.info "[${joinPoint.signature.toShortString()}] Response: ${result}"
  }
}
