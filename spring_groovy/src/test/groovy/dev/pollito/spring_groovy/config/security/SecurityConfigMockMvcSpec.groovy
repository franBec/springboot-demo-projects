package dev.pollito.spring_groovy.config.security

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = ["/sakila-schema.sql", "/sakila-data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class SecurityConfigMockMvcSpec extends Specification {

  @Autowired
  MockMvc mockMvc

  private static final Map<HttpMethod, Closure<MockHttpServletRequestBuilder>> REQUEST_BUILDERS = [
    (HttpMethod.GET) : { String p -> get(p) },
    (HttpMethod.POST): { String p ->
      post(p)
    }
  ]

  private void assertEndpoint(HttpMethod method, String path, String body, int expectedStatus) {
    def builder = REQUEST_BUILDERS[method]
    if (!builder) {
      throw new IllegalArgumentException("Unsupported method: " + method)
    }

    MockHttpServletRequestBuilder request = builder.call(path)

    if (body != null && !body.isBlank()) {
      request = request.contentType(APPLICATION_JSON).content(body)
    }

    mockMvc.perform(request.accept(APPLICATION_JSON))
        .andExpect(status().is(expectedStatus))
  }

  @Unroll
  def "#method #path returns #expectedStatus"() {
    when:
    assertEndpoint(method, path, body, expectedStatus)

    then:
    noExceptionThrown()

    where:
    method                  | path             | body                                      | expectedStatus
    HttpMethod.GET          | "/api/films"     | null                                      | 200
    HttpMethod.GET          | "/api/films/1"   | null                                      | 200
    HttpMethod.POST         | "/api/films"     | "{}"                                      | 401
    HttpMethod.POST         | "/api/auth/login"| '{"username":"Mike","password":"password"}'| 200
    HttpMethod.GET          | "/api/auth/me"   | null                                      | 401
    HttpMethod.GET          | "/actuator/health"| null                                     | 200
  }
}
