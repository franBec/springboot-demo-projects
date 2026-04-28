package dev.pollito.spring_java.test.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
}
