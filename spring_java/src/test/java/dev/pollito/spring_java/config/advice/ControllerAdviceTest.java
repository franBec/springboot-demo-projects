package dev.pollito.spring_java.config.advice;

import static dev.pollito.spring_java.test.util.ApiResponseMatchers.hasErrorFields;
import static dev.pollito.spring_java.test.util.ApiResponseMatchers.hasStandardApiResponseFields;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class ControllerAdviceTest {

  private MockMvc mockMvc;
  private final HttpServletRequest request = mock(HttpServletRequest.class);

  @RestController
  @RequestMapping("/fake")
  static class FakeController {

    @GetMapping("/not-found")
    public void throwNoResourceFoundException() throws NoResourceFoundException {
      throw new NoResourceFoundException(GET, "/fake", "no-resource-found");
    }

    @GetMapping("/error")
    public void throwException() throws Exception {
      throw new Exception("Test exception");
    }

    @GetMapping("/bad-request")
    public void throwConstraintViolationException() {
      throw new ConstraintViolationException("Constraint violation", Set.of());
    }
  }

  @BeforeEach
  void setUp() {
    mockMvc =
        standaloneSetup(new FakeController())
            .setControllerAdvice(new ControllerAdvice(request))
            .build();
  }

  @Test
  void whenNoResourceFoundException_thenReturnsNotFound() throws Exception {
    HttpStatus status = NOT_FOUND;
    String expectedInstance = "/fake/not-found";

    when(request.getRequestURI()).thenReturn(expectedInstance);
    mockMvc
        .perform(get(expectedInstance))
        .andExpect(status().isNotFound())
        .andExpect(hasStandardApiResponseFields(expectedInstance, status))
        .andExpect(hasErrorFields(status));
  }

  @Test
  void whenException_thenReturnsInternalServerError() throws Exception {
    HttpStatus status = INTERNAL_SERVER_ERROR;
    String expectedInstance = "/fake/error";

    when(request.getRequestURI()).thenReturn(expectedInstance);
    mockMvc
        .perform(get(expectedInstance))
        .andExpect(status().isInternalServerError())
        .andExpect(hasStandardApiResponseFields(expectedInstance, status))
        .andExpect(hasErrorFields(status));
  }

  @Test
  void whenConstraintViolationException_thenReturnsBadRequest() throws Exception {
    HttpStatus status = BAD_REQUEST;
    String expectedInstance = "/fake/bad-request";

    when(request.getRequestURI()).thenReturn(expectedInstance);
    mockMvc
        .perform(get(expectedInstance))
        .andExpect(status().isBadRequest())
        .andExpect(hasStandardApiResponseFields(expectedInstance, status))
        .andExpect(hasErrorFields(status));
  }
}
