package dev.pollito.spring_kotlin.config.log

import io.opentelemetry.api.trace.Span.current
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC.put
import org.slf4j.MDC.remove
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(LOWEST_PRECEDENCE - 1)
class TraceIdFilter : OncePerRequestFilter() {

  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
  ) {
    val spanContext = current().spanContext
    if (spanContext.isValid) {
      put("trace_id", spanContext.traceId)
      put("span_id", spanContext.spanId)
      put("trace_flags", if (spanContext.traceFlags.isSampled) "01" else "00")
    }

    try {
      filterChain.doFilter(request, response)
    } finally {
      remove("trace_id")
      remove("span_id")
      remove("trace_flags")
    }
  }
}
