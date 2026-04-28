package dev.pollito.spring_kotlin.config.security.handler

import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.OffsetDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

object AuthenticationErrorResponseWriter {

  fun write(
      request: HttpServletRequest,
      response: HttpServletResponse,
      status: HttpStatus,
      detail: String,
  ) {
    response.status = status.value()
    response.contentType = APPLICATION_JSON_VALUE

    val json =
        "{" +
            "\"title\":\"${escapeJson(status.reasonPhrase)}\"," +
            "\"detail\":\"${escapeJson(detail)}\"," +
            "\"instance\":\"${escapeJson(request.requestURI)}\"," +
            "\"status\":${status.value()}," +
            "\"timestamp\":\"${OffsetDateTime.now()}\"," +
            "\"trace\":\"${Span.current().spanContext.traceId}\"" +
            "}"

    response.writer.print(json)
  }

  private fun escapeJson(value: String): String {
    return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
  }
}
