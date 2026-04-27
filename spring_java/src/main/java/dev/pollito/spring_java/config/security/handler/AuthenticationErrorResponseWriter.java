package dev.pollito.spring_java.config.security.handler;

import static io.opentelemetry.api.trace.Span.current;
import static java.time.OffsetDateTime.now;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

final class AuthenticationErrorResponseWriter {

  private AuthenticationErrorResponseWriter() {}

  static void write(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull HttpStatus status,
      @NonNull String detail)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String json =
        "{"
            + "\"title\":\""
            + escapeJson(status.getReasonPhrase())
            + "\","
            + "\"detail\":\""
            + escapeJson(detail)
            + "\","
            + "\"instance\":\""
            + escapeJson(request.getRequestURI())
            + "\","
            + "\"status\":"
            + status.value()
            + ","
            + "\"timestamp\":\""
            + now()
            + "\","
            + "\"trace\":\""
            + current().getSpanContext().getTraceId()
            + "\""
            + "}";

    response.getWriter().print(json);
  }

  private static @NonNull String escapeJson(@NonNull String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
  }
}
