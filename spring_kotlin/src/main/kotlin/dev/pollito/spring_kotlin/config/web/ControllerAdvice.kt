package dev.pollito.spring_kotlin.config.web

import io.github.oshai.kotlinlogging.KotlinLogging
import io.opentelemetry.api.trace.Span.current
import java.time.Instant.now
import java.time.format.DateTimeFormatter.ISO_INSTANT
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class ControllerAdvice {
  companion object {
    private fun buildProblemDetail(e: Exception, status: HttpStatus): ProblemDetail {
      val exceptionSimpleName = e.javaClass.simpleName
      val logMessage = "$exceptionSimpleName being handled"

      when {
        status.is5xxServerError -> log.error(e) { logMessage }
        status.is4xxClientError -> log.warn(e) { logMessage }
        else -> log.info(e) { logMessage }
      }

      val problemDetail = ProblemDetail.forStatusAndDetail(status, e.localizedMessage)
      problemDetail.setProperty("timestamp", ISO_INSTANT.format(now()))
      problemDetail.setProperty("trace", current().spanContext.traceId)

      return problemDetail
    }
  }

  @ExceptionHandler(Exception::class)
  fun handle(e: Exception): ProblemDetail {
    return buildProblemDetail(e, INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(NoResourceFoundException::class)
  fun handle(e: NoResourceFoundException): ProblemDetail {
    return buildProblemDetail(e, NOT_FOUND)
  }
}
