package dev.pollito.spring_groovy.config.advice

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

import dev.pollito.spring_groovy.test.util.ApiResponseMatchers
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.resource.NoResourceFoundException
import spock.lang.Specification

class ControllerAdviceSpec extends Specification implements ApiResponseMatchers {
  MockMvc mockMvc
  HttpServletRequest request = Mock()

  @RestController
  @RequestMapping("/fake")
  static class FakeController {

    @GetMapping("/not-found")
    static void throwNoResourceFoundException() throws NoResourceFoundException {
      throw new NoResourceFoundException(GET, "/fake", "no-resource-found")
    }

    @GetMapping("/error")
    static void throwException() throws Exception {
      throw new Exception("Test exception")
    }

    @GetMapping("/bad-request")
    static void throwConstraintViolationException() {
      throw new ConstraintViolationException("Constraint violation", Set.of())
    }
  }

  def setup() {
    mockMvc = standaloneSetup(new FakeController())
        .setControllerAdvice(new ControllerAdvice(request))
        .build()
  }

  def "when NoResourceFoundException then returns NOT_FOUND"() {
    given:
    HttpStatus httpStatus = NOT_FOUND
    String expectedInstance = "/fake/not-found"
    request.getRequestURI() >> expectedInstance

    expect:
    mockMvc.perform(get(expectedInstance))
        .andExpect(status().isNotFound())
        .andExpect(hasStandardApiResponseFields(expectedInstance, httpStatus))
        .andExpect(hasErrorFields(httpStatus))
  }

  def "when Exception then returns INTERNAL_SERVER_ERROR"() {
    given:
    HttpStatus httpStatus = INTERNAL_SERVER_ERROR
    String expectedInstance = "/fake/error"
    request.getRequestURI() >> expectedInstance

    expect:
    mockMvc.perform(get(expectedInstance))
        .andExpect(status().isInternalServerError())
        .andExpect(hasStandardApiResponseFields(expectedInstance, httpStatus))
        .andExpect(hasErrorFields(httpStatus))
  }

  def "when ConstraintViolationException then returns BAD_REQUEST"() {
    HttpStatus httpStatus = BAD_REQUEST
    String expectedInstance = "/fake/bad-request"
    request.getRequestURI() >> expectedInstance

    expect:
    mockMvc.perform(get(expectedInstance))
        .andExpect(status().isBadRequest())
        .andExpect(hasStandardApiResponseFields(expectedInstance, httpStatus))
        .andExpect(hasErrorFields(httpStatus))
  }
}
