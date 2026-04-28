package dev.pollito.spring_groovy.config.web

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

import dev.pollito.spring_groovy.test.util.MockMvcResultMatchersTrait
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import java.util.NoSuchElementException
import org.springframework.core.MethodParameter
import org.springframework.security.core.AuthenticationException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.resource.NoResourceFoundException
import spock.lang.Specification
import spock.lang.Unroll

class ControllerAdviceMockMvcSpec extends Specification implements MockMvcResultMatchersTrait {
  MockMvc mockMvc
  HttpServletRequest request = Mock()

  @RestController
  @RequestMapping("/fake")
  static class FakeController {

    @GetMapping("/not-found")
    @SuppressWarnings("unused")
    static void throwNoResourceFoundException() throws NoResourceFoundException {
      throw new NoResourceFoundException(GET, "/fake", "no-resource-found")
    }

    @GetMapping("/error")
    @SuppressWarnings("unused")
    static void throwException() throws Exception {
      throw new Exception("Test exception")
    }

    @GetMapping("/bad-request")
    @SuppressWarnings("unused")
    static void throwConstraintViolationException() {
      throw new ConstraintViolationException("Constraint violation", Set.of())
    }

    @GetMapping("/method-argument-not-valid")
    @SuppressWarnings("unused")
    static void throwMethodArgumentNotValidException() throws MethodArgumentNotValidException {
      throw new MethodArgumentNotValidException(
      new MethodParameter(String.class.getMethod("valueOf", Object.class), 0),
      new BeanPropertyBindingResult(new Object(), "object")
      )
    }

    @GetMapping("/no-such-element")
    @SuppressWarnings("unused")
    static void throwNoSuchElementException() {
      throw new NoSuchElementException("No such element")
    }

    @GetMapping("/authentication")
    @SuppressWarnings("unused")
    static void throwAuthenticationException() {
      throw new AuthenticationException("Authentication failed") {}
    }
  }

  def setup() {
    mockMvc = standaloneSetup(new FakeController())
        .setControllerAdvice(new ControllerAdvice(request))
        .build()
  }

  @Unroll
  def "#exceptionType returns #httpStatus"() {
    given:
    request.getRequestURI() >> endpoint

    expect:
    mockMvc.perform(get(endpoint))
        .andExpect(status().is(httpStatus.value()))
        .andExpect(hasStandardApiResponseFields(endpoint, httpStatus))
        .andExpect(hasErrorFields(httpStatus))

    where:
    endpoint                            | httpStatus            || exceptionType
    "/fake/not-found"                   | NOT_FOUND             || "NoResourceFoundException"
    "/fake/error"                       | INTERNAL_SERVER_ERROR || "Exception"
    "/fake/bad-request"                 | BAD_REQUEST           || "ConstraintViolationException"
    "/fake/method-argument-not-valid"   | BAD_REQUEST           || "MethodArgumentNotValidException"
    "/fake/no-such-element"             | NOT_FOUND             || "NoSuchElementException"
    "/fake/authentication"              | UNAUTHORIZED          || "AuthenticationException"
  }
}