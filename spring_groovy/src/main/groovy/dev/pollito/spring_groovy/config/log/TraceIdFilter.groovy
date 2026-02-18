package dev.pollito.spring_groovy.config.log

import static org.slf4j.MDC.put
import static org.slf4j.MDC.remove

import groovy.transform.CompileStatic
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanContext
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(2147483646)
@CompileStatic
class TraceIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  throws ServletException, IOException {

    SpanContext spanContext = Span.current().spanContext
    if (spanContext.valid) {
      put("trace_id", spanContext.traceId)
      put("span_id", spanContext.spanId)
      put("trace_flags", spanContext.traceFlags.sampled ? "01" : "00")
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
