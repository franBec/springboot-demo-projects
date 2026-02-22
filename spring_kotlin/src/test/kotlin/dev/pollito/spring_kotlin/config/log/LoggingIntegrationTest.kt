package dev.pollito.spring_kotlin.config.log

import com.ninjasquad.springmockk.MockkBean
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FindByIdPortIn
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension::class)
class LoggingIntegrationTest {

  companion object {
    private const val FILM_BY_ID_PATH = "/api/films/{id}"
  }

  @Autowired private lateinit var mockMvc: MockMvc
  @MockkBean private lateinit var findByIdPortIn: FindByIdPortIn

  @Test
  fun `when request then all logging components work together`(output: CapturedOutput) {
    val filmId = 1
    val film = mockk<Film>(relaxed = true)
    every { film.id } returns filmId
    every { findByIdPortIn.findById(filmId) } returns film

    mockMvc
        .get(FILM_BY_ID_PATH, filmId) {
          accept(APPLICATION_JSON)
          header("Authorization", "Bearer secret-token")
          header("X-Api-Key", "my-secret-key")
        }
        .andExpect { status { isOk() } }

    val logOutput = output.out

    assertLogFilterOutput(logOutput)
    assertLogAspectOutput(logOutput)
    assertMaskingPatternLayoutOutput(logOutput)
    assertTraceIdFilterOutput(logOutput)
  }

  private fun assertLogFilterOutput(logOutput: String) {
    assert(logOutput.contains(">>>> Method: GET; URI: /api/films/1")) {
      "LogFilter should log request details"
    }
    assert(logOutput.contains("<<<< Response Status: 200")) {
      "LogFilter should log response status"
    }
  }

  private fun assertLogAspectOutput(logOutput: String) {
    assert(logOutput.contains("findById(..)] Args:")) { "LogAspect should log method args" }
    assert(logOutput.contains("findById(..)] Response:")) { "LogAspect should log response" }
  }

  private fun assertMaskingPatternLayoutOutput(logOutput: String) {
    assert(!logOutput.contains("secret-token")) {
      "MaskingPatternLayout should mask Authorization value"
    }
    assert(!logOutput.contains("my-secret-key")) {
      "MaskingPatternLayout should mask X-Api-Key value"
    }
    assert(logOutput.contains("Authorization: ****")) {
      "MaskingPatternLayout should show masked Authorization"
    }
    assert(logOutput.contains("X-Api-Key: ****")) {
      "MaskingPatternLayout should show masked X-Api-Key"
    }
  }

  private fun assertTraceIdFilterOutput(logOutput: String) {
    assert(logOutput.contains("trace_id=")) { "TraceIdFilter should add trace_id to MDC" }
    assert(logOutput.contains("span_id=")) { "TraceIdFilter should add span_id to MDC" }
    assert(logOutput.contains("trace_flags=")) { "TraceIdFilter should add trace_flags to MDC" }
  }
}
