package dev.pollito.spring_java.test.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultMatcher;

public final class MockMvcResultMatchers {

  private MockMvcResultMatchers() {}

  @Contract(pure = true)
  public static @NonNull ResultMatcher hasStandardApiResponseFields(
      String expectedInstance, HttpStatus expectedStatus) {
    return result -> {
      jsonPath("$.instance").value(expectedInstance).match(result);
      jsonPath("$.status").value(expectedStatus.value()).match(result);
      jsonPath("$.timestamp").exists().match(result);
      jsonPath("$.trace").exists().match(result);
    };
  }

  @Contract(pure = true)
  public static @NonNull ResultMatcher hasErrorFields(HttpStatus expectedStatus) {
    return result -> jsonPath("$.title").value(expectedStatus.getReasonPhrase()).match(result);
  }

  @Contract(pure = true)
  public static @NonNull ResultMatcher hasPageFields() {
    return result -> {
      jsonPath("$.data.content").isArray().match(result);
      jsonPath("$.data.pageable.pageNumber").isNumber().match(result);
      jsonPath("$.data.pageable.pageSize").isNumber().match(result);
      jsonPath("$.data.totalElements").isNumber().match(result);
      jsonPath("$.data.totalPages").isNumber().match(result);
    };
  }

  @Contract(pure = true)
  public static @NonNull ResultMatcher hasStandardWebResponseFields(
      String expectedInstance, HttpStatus expectedStatus) {
    return result -> {
      model().attribute("status", expectedStatus.value()).match(result);
      model().attribute("error", expectedStatus.getReasonPhrase()).match(result);
      model().attribute("instance", expectedInstance).match(result);
      model().attributeExists("trace").match(result);
    };
  }

  @Contract(pure = true)
  public static @NonNull ResultMatcher hasWebMessageField() {
    return result -> model().attributeExists("message").match(result);
  }
}
