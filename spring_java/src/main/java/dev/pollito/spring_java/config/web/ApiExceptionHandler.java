package dev.pollito.spring_java.config.web;

import static io.opentelemetry.api.trace.Span.current;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

import dev.pollito.spring_java.sakila.generated.model.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice(
    basePackages = {
      "dev.pollito.spring_java.sakila.film.adapter.in.rest",
      "dev.pollito.spring_java.sakila.auth.adapter.in.rest",
      "dev.pollito.spring_java.config.web"
    })
@RequiredArgsConstructor
@Slf4j
public class ApiExceptionHandler {
  private final HttpServletRequest request;

  private @NonNull ResponseEntity<Error> buildProblemDetail(
      @NonNull Exception e, @NonNull HttpStatus status) {
    String exceptionSimpleName = e.getClass().getSimpleName();
    String logMessage = "{} being handled";

    switch (status.series()) {
      case SERVER_ERROR -> log.error(logMessage, exceptionSimpleName, e);
      case CLIENT_ERROR -> log.warn(logMessage, exceptionSimpleName, e);
      default -> log.info(logMessage, exceptionSimpleName, e);
    }

    return status(status)
        .body(
            new Error()
                .detail(e.getLocalizedMessage())
                .instance(request.getRequestURI())
                .status(status.value())
                .timestamp(now())
                .title(status.getReasonPhrase())
                .trace(current().getSpanContext().getTraceId()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handle(Exception e) {
    return buildProblemDetail(e, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Error> handle(NoResourceFoundException e) {
    return buildProblemDetail(e, NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Error> handle(ConstraintViolationException e) {
    return buildProblemDetail(e, BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handle(MethodArgumentNotValidException e) {
    return buildProblemDetail(e, BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Error> handle(NoSuchElementException e) {
    return buildProblemDetail(e, NOT_FOUND);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Error> handle(AuthenticationException e) {
    return buildProblemDetail(e, UNAUTHORIZED);
  }
}
