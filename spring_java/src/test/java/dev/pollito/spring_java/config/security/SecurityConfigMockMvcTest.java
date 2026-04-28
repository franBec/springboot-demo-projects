package dev.pollito.spring_java.config.security;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(
    scripts = {"/sakila-schema.sql", "/sakila-data.sql"},
    executionPhase = BEFORE_TEST_CLASS)
class SecurityConfigMockMvcTest {

  @Autowired private MockMvc mockMvc;

  private void assertEndpoint(
      @NonNull HttpMethod method, String path, String body, int expectedStatus) throws Exception {
    MockHttpServletRequestBuilder request =
        switch (method.name()) {
          case "GET" -> get(path);
          case "POST" -> post(path);
          default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };

    if (body != null && !body.isBlank()) {
      request = request.contentType(APPLICATION_JSON).content(body);
    }

    mockMvc.perform(request.accept(APPLICATION_JSON)).andExpect(status().is(expectedStatus));
  }

  @Nested
  @DisplayName("Film endpoints")
  class Films {

    @ParameterizedTest(name = "{0} {1} returns {3}")
    @CsvSource(
        delimiter = '|',
        textBlock =
            """
            GET  | /api/films   |              | 200
            GET  | /api/films/1 |              | 200
            POST | /api/films   | {}           | 401
            """)
    void filmEndpoints(String method, String path, String body, int expectedStatus)
        throws Exception {
      assertEndpoint(HttpMethod.valueOf(method), path, body, expectedStatus);
    }
  }

  @Nested
  @DisplayName("Auth endpoints")
  class Auth {

    @ParameterizedTest(name = "{0} {1} returns {3}")
    @CsvSource(
        delimiter = '|',
        textBlock =
            """
            POST | /api/auth/login | {"username":"Mike","password":"password"} | 200
            GET  | /api/auth/me    |                                           | 401
            """)
    void authEndpoints(String method, String path, String body, int expectedStatus)
        throws Exception {
      assertEndpoint(HttpMethod.valueOf(method), path, body, expectedStatus);
    }
  }

  @Nested
  @DisplayName("Actuator endpoints")
  class Actuator {

    @ParameterizedTest(name = "{0} {1} returns {3}")
    @CsvSource(
        delimiter = '|',
        textBlock =
            """
            GET | /actuator/health | | 200
            """)
    void actuatorEndpoints(String method, String path, String body, int expectedStatus)
        throws Exception {
      assertEndpoint(HttpMethod.valueOf(method), path, body, expectedStatus);
    }
  }

  @Nested
  @DisplayName("H2 Console")
  class H2Console {

    @ParameterizedTest(name = "{0} {1} returns {3}")
    @CsvSource(
        delimiter = '|',
        textBlock =
            """
            GET | /h2-console | | 404
            """)
    void h2ConsoleEndpoints(String method, String path, String body, int expectedStatus)
        throws Exception {
      assertEndpoint(HttpMethod.valueOf(method), path, body, expectedStatus);
    }
  }

  @Nested
  @DisplayName("Error endpoint")
  class ErrorEndpoint {

    @ParameterizedTest(name = "{0} {1} returns {3}")
    @CsvSource(
        delimiter = '|',
        textBlock =
            """
            GET | /error | | 500
            """)
    void errorEndpoint(String method, String path, String body, int expectedStatus)
        throws Exception {
      assertEndpoint(HttpMethod.valueOf(method), path, body, expectedStatus);
    }
  }
}
