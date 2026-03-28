package dev.pollito.spring_groovy.config.web

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR
import static org.springframework.http.ResponseEntity.status

import dev.pollito.spring_groovy.sakila.generated.model.Error
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

  private ResponseEntity<Error> buildErrorResponse(Exception e, HttpStatus httpStatus) {
    def exceptionSimpleName = e.class.simpleName
    def logMessage = "${exceptionSimpleName} being handled"

    switch (httpStatus.series()) {
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

    status(httpStatus).body(
        new Error()
        .detail(e.getLocalizedMessage())
        .instance(request.requestURI)
        .status(httpStatus.value())
        .timestamp(now())
        .title(httpStatus.reasonPhrase)
        .trace(Span.current().spanContext.traceId)
        )
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Error> handle(Exception e) {
    buildErrorResponse(e, INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(NoResourceFoundException)
  ResponseEntity<Error> handle(NoResourceFoundException e) {
    buildErrorResponse(e, NOT_FOUND)
  }

  @ExceptionHandler(ConstraintViolationException)
  ResponseEntity<Error> handle(ConstraintViolationException e) {
    buildErrorResponse(e, BAD_REQUEST)
  }

  @ExceptionHandler(MethodArgumentNotValidException)
  ResponseEntity<Error> handle(MethodArgumentNotValidException e) {
    buildErrorResponse(e, BAD_REQUEST)
  }
}
