package dev.pollito.spring_groovy.config.advice

import static java.time.Instant.now
import static java.time.format.DateTimeFormatter.ISO_INSTANT
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.opentelemetry.api.trace.Span
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
@Slf4j
@CompileStatic
class ControllerAdvice {
  private static ProblemDetail buildProblemDetail(Exception e, HttpStatus status) {
    def exceptionSimpleName = e.class.simpleName
    def logMessage = "${exceptionSimpleName} being handled"

    switch (status.series()) {
      case SERVER_ERROR:
        log.error(logMessage, e)
        break
      case CLIENT_ERROR:
        log.warn(logMessage, e)
        break
      default:
        log.info(logMessage, e)
        break
    }

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getLocalizedMessage())
    problemDetail.setProperty("timestamp", ISO_INSTANT.format(now()))
    problemDetail.setProperty("trace", Span.current().spanContext.traceId)

    problemDetail
  }

  @ExceptionHandler(Exception.class)
  ProblemDetail handle(Exception e) {
    buildProblemDetail(e, INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(NoResourceFoundException)
  ProblemDetail handle(NoResourceFoundException e) {
    buildProblemDetail(e, NOT_FOUND)
  }
}
