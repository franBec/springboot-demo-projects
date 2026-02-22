package dev.pollito.spring_java.config.log;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.in.FindByIdPortIn;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class LoggingIntegrationTest {

  private static final String FILM_BY_ID_PATH = "/api/films/{id}";

  @SuppressWarnings("unused")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("unused")
  @MockitoBean
  private FindByIdPortIn findByIdPortIn;

  @Test
  void whenRequestThenAllLoggingComponentsWorkTogether(@NonNull CapturedOutput output)
      throws Exception {
    Integer filmId = 1;
    Film film = mock(Film.class);
    when(film.getId()).thenReturn(filmId);
    when(findByIdPortIn.findById(filmId)).thenReturn(film);

    mockMvc
        .perform(
            get(FILM_BY_ID_PATH, filmId)
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer secret-token")
                .header("X-Api-Key", "my-secret-key"))
        .andExpect(status().isOk());

    String logOutput = output.getOut();

    assertLogFilterOutput(logOutput);
    assertLogAspectOutput(logOutput);
    assertMaskingPatternLayoutOutput(logOutput);
    assertTraceIdFilterOutput(logOutput);
  }

  private void assertLogFilterOutput(@NonNull String logOutput) {
    assert logOutput.contains(">>>> Method: GET; URI: /api/films/1")
        : "LogFilter should log request details";
    assert logOutput.contains("<<<< Response Status: 200") : "LogFilter should log response status";
  }

  private void assertLogAspectOutput(@NonNull String logOutput) {
    assert logOutput.contains("findById(..)] Args:") : "LogAspect should log method args";
    assert logOutput.contains("findById(..)] Response:") : "LogAspect should log response";
  }

  private void assertMaskingPatternLayoutOutput(@NonNull String logOutput) {
    assert !logOutput.contains("secret-token")
        : "MaskingPatternLayout should mask Authorization value";
    assert !logOutput.contains("my-secret-key")
        : "MaskingPatternLayout should mask X-Api-Key value";
    assert logOutput.contains("Authorization: ****")
        : "MaskingPatternLayout should show masked Authorization";
    assert logOutput.contains("X-Api-Key: ****")
        : "MaskingPatternLayout should show masked X-Api-Key";
  }

  private void assertTraceIdFilterOutput(@NonNull String logOutput) {
    assert logOutput.contains("trace_id=") : "TraceIdFilter should add trace_id to MDC";
    assert logOutput.contains("span_id=") : "TraceIdFilter should add span_id to MDC";
    assert logOutput.contains("trace_flags=") : "TraceIdFilter should add trace_flags to MDC";
  }
}
