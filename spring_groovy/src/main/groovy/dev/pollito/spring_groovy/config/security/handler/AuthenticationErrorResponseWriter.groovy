package dev.pollito.spring_groovy.config.security.handler

import static io.opentelemetry.api.trace.Span.current
import static java.time.OffsetDateTime.now

import groovy.transform.CompileStatic
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@CompileStatic
final class AuthenticationErrorResponseWriter {

  private AuthenticationErrorResponseWriter() {}

  static void write(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String detail) {
    response.status = status.value()
    response.contentType = MediaType.APPLICATION_JSON_VALUE

    String json = "{\"title\":\"" + escapeJson(status.reasonPhrase) + "\"," +
        "\"detail\":\"" + escapeJson(detail) + "\"," +
        "\"instance\":\"" + escapeJson(request.requestURI) + "\"," +
        "\"status\":" + status.value() + "," +
        "\"timestamp\":\"" + now() + "\"," +
        "\"trace\":\"" + current().spanContext.traceId + "\"}"

    response.writer.print(json)
  }

  private static String escapeJson(String value) {
    value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
  }
}
