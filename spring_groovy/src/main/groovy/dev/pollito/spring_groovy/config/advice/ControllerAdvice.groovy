package dev.pollito.spring_groovy.config.advice

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR

import dev.pollito.spring_groovy.generated.model.Error
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
@Slf4j
@CompileStatic
class ControllerAdvice {

  private final HttpServletRequest request

  ControllerAdvice(HttpServletRequest request) {
    this.request = request
  }

  private ResponseEntity<Error> buildProblemDetail(Exception e, HttpStatus status) {
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

    ResponseEntity.status(status)
        .body(
        new Error(
        detail: e.localizedMessage,
        instance: request.requestURI,
        timestamp: now(),
        title: status.reasonPhrase,
        trace: Span.current().spanContext.traceId,
        status: status.value(),
        )
        )
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Error> handle(Exception e) {
    buildProblemDetail(e, INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(NoResourceFoundException)
  ResponseEntity<Error> handle(NoResourceFoundException e) {
    buildProblemDetail(e, NOT_FOUND)
  }
}
