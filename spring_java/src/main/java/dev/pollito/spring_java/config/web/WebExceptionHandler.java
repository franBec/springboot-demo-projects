package dev.pollito.spring_java.config.web;

import static io.opentelemetry.api.trace.Span.current;
import static org.springframework.http.HttpStatus.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice(
    basePackages = {
      "dev.pollito.spring_java.sakila.film.adapter.in.web",
      "dev.pollito.spring_java.sakila.auth.adapter.in.web"
    })
@Slf4j
public class WebExceptionHandler {

  @Autowired private HttpServletRequest request;

  private void logException(@NonNull Exception e, @NonNull HttpStatus status) {
    String exceptionSimpleName = e.getClass().getSimpleName();
    String logMessage = "{} being handled";

    switch (status.series()) {
      case SERVER_ERROR -> log.error(logMessage, exceptionSimpleName, e);
      case CLIENT_ERROR -> log.warn(logMessage, exceptionSimpleName, e);
      default -> log.info(logMessage, exceptionSimpleName, e);
    }
  }

  private @NonNull ModelAndView buildErrorView(
      Exception e, HttpStatus status, @NonNull HttpServletResponse response) {
    logException(e, status);
    response.setStatus(status.value());

    ModelAndView mav = new ModelAndView("error");
    mav.addObject("status", status.value());
    mav.addObject("error", status.getReasonPhrase());
    mav.addObject("message", e.getLocalizedMessage());
    mav.addObject("trace", current().getSpanContext().getTraceId());
    mav.addObject("instance", request.getRequestURI());
    return mav;
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView handle(Exception e, HttpServletResponse response) {
    return buildErrorView(e, INTERNAL_SERVER_ERROR, response);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ModelAndView handle(NoResourceFoundException e, HttpServletResponse response) {
    return buildErrorView(e, NOT_FOUND, response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ModelAndView handle(ConstraintViolationException e, HttpServletResponse response) {
    return buildErrorView(e, BAD_REQUEST, response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ModelAndView handle(MethodArgumentNotValidException e, HttpServletResponse response) {
    return buildErrorView(e, BAD_REQUEST, response);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ModelAndView handle(NoSuchElementException e, HttpServletResponse response) {
    return buildErrorView(e, NOT_FOUND, response);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ModelAndView handle(AuthenticationException e, HttpServletResponse response) {
    return buildErrorView(e, UNAUTHORIZED, response);
  }
}
