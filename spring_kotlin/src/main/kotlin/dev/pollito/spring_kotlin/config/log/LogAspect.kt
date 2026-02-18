package dev.pollito.spring_kotlin.config.log

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Aspect
@Component
class LogAspect {

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  fun controllerPublicMethodsPointcut() {
    // Method is empty as this is just a Pointcut declaration
  }

  @Before("controllerPublicMethodsPointcut()")
  fun logBefore(joinPoint: JoinPoint) {
    log.info {
      "[${joinPoint.signature.toShortString()}] Args: ${joinPoint.args.contentToString()}"
    }
  }

  @AfterReturning(pointcut = "controllerPublicMethodsPointcut()", returning = "result")
  fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
    log.info { "[${joinPoint.signature.toShortString()}] Response: $result" }
  }
}
