package dev.pollito.spring_groovy.config.log

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindByIdPortIn
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class LoggingIntegrationSpec extends Specification {

  static final String FILM_BY_ID_PATH = "/api/films/{id}"

  @Autowired
  MockMvc mockMvc

  @SpringBean
  FindByIdPortIn findByIdPortIn = Stub()

  ByteArrayOutputStream outputCapture
  PrintStream originalOut
  PrintStream originalErr

  def setup() {
    outputCapture = new ByteArrayOutputStream()
    originalOut = System.out
    originalErr = System.err
    def printStream = new PrintStream(outputCapture)
    System.setOut(printStream)
    System.setErr(printStream)
  }

  def cleanup() {
    System.setOut(originalOut)
    System.setErr(originalErr)
  }

  def "when request then all logging components work together"() {
    given:
    def filmId = 1
    def film = Stub(Film) {getId() >> filmId}
    findByIdPortIn.findById(filmId) >> film

    when:
    mockMvc.perform(
        get(FILM_BY_ID_PATH, filmId)
        .accept(APPLICATION_JSON)
        .header("Authorization", "Bearer secret-token")
        .header("X-Api-Key", "my-secret-key"))
        .andExpect(status().isOk())

    then: "LogFilter logs request and response details"
    def logOutput = outputCapture.toString()
    logOutput.contains(">>>> Method: GET; URI: /api/films/1")
    logOutput.contains("<<<< Response Status: 200")

    and: "LogAspect logs method args and response"
    logOutput.contains("findById(..)] Args:")
    logOutput.contains("findById(..)] Response:")

    and: "MaskingPatternLayout masks sensitive headers"
    !logOutput.contains("secret-token")
    !logOutput.contains("my-secret-key")
    logOutput.contains("Authorization: ****")
    logOutput.contains("X-Api-Key: ****")

    and: "TraceIdFilter adds trace information to MDC"
    logOutput.contains("trace_id=")
    logOutput.contains("span_id=")
    logOutput.contains("trace_flags=")
  }
}
