package dev.pollito.spring_java.config.web;

import static dev.pollito.spring_java.test.util.MockMvcResultMatchers.hasStandardWebResponseFields;
import static dev.pollito.spring_java.test.util.MockMvcResultMatchers.hasWebMessageField;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class WebExceptionHandlerMockMvcTest {

  private MockMvc mockMvc;
  private final HttpServletRequest request = mock(HttpServletRequest.class);

  @org.springframework.web.bind.annotation.ControllerAdvice
  private static class TestExceptionHandler extends WebExceptionHandler {}

  @RestController
  @RequestMapping("/web/fake")
  private static class FakeController {

    @GetMapping("/not-found")
    @SuppressWarnings("unused")
    public void throwNoResourceFoundException() throws NoResourceFoundException {
      throw new NoResourceFoundException(GET, "/web/fake", "no-resource-found");
    }

    @GetMapping("/error")
    @SuppressWarnings("unused")
    public void throwException() throws Exception {
      throw new Exception("Test exception");
    }

    @GetMapping("/bad-request")
    @SuppressWarnings("unused")
    public void throwConstraintViolationException() {
      throw new ConstraintViolationException("Constraint violation", Set.of());
    }

    @GetMapping("/method-arg-not-valid")
    @SuppressWarnings({"unused"})
    public void throwMethodArgumentNotValidException() throws Exception {
      throw new MethodArgumentNotValidException(
          new MethodParameter(
              FakeController.class.getMethod("throwMethodArgumentNotValidException"), -1),
          mock(BindingResult.class));
    }

    @GetMapping("/no-such-element")
    @SuppressWarnings("unused")
    public void throwNoSuchElementException() {
      throw new NoSuchElementException("No such element");
    }

    @GetMapping("/unauthorized")
    @SuppressWarnings("unused")
    public void throwAuthenticationException() throws AuthenticationException {
      throw new AuthenticationException("Unauthorized") {};
    }
  }

  @BeforeEach
  void setUp() throws Exception {
    TestExceptionHandler handler = new TestExceptionHandler();
    Field requestField = WebExceptionHandler.class.getDeclaredField("request");
    requestField.setAccessible(true);
    requestField.set(handler, request);
    mockMvc =
        standaloneSetup(new FakeController())
            .setControllerAdvice(handler)
            .setViewResolvers(
                (String viewName, Locale locale) -> {
                  if ("error".equals(viewName)) {
                    return new View() {
                      @Override
                      public String getContentType() {
                        return "text/html";
                      }

                      @Override
                      public void render(
                          java.util.Map<String, ?> model,
                          jakarta.servlet.http.@NonNull HttpServletRequest request,
                          jakarta.servlet.http.@NonNull HttpServletResponse response) {
                        response.setStatus(
                            model.containsKey("status") ? (Integer) model.get("status") : 500);
                      }
                    };
                  }
                  return null;
                })
            .build();
  }

  static @NonNull Stream<Arguments> testCases() {
    return Stream.of(
        Arguments.of("/web/fake/not-found", NOT_FOUND),
        Arguments.of("/web/fake/error", INTERNAL_SERVER_ERROR),
        Arguments.of("/web/fake/bad-request", BAD_REQUEST),
        Arguments.of("/web/fake/method-arg-not-valid", BAD_REQUEST),
        Arguments.of("/web/fake/no-such-element", NOT_FOUND),
        Arguments.of("/web/fake/unauthorized", UNAUTHORIZED));
  }

  @ParameterizedTest
  @MethodSource("testCases")
  void exceptionHandlingReturnsCorrectStatus(String path, @NonNull HttpStatus expectedStatus)
      throws Exception {
    when(request.getRequestURI()).thenReturn(path);
    mockMvc
        .perform(get(path))
        .andExpect(status().is(expectedStatus.value()))
        .andExpect(hasStandardWebResponseFields(path, expectedStatus))
        .andExpect(hasWebMessageField());
  }
}
