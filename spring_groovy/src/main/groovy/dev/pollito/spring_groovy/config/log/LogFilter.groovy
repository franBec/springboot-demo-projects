package dev.pollito.spring_groovy.config.log

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(2147483647)
@Slf4j
@CompileStatic
class LogFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  throws ServletException, IOException {
    logRequestDetails(request)
    filterChain.doFilter(request, response)
    logResponseDetails(response)
  }

  private static void logRequestDetails(HttpServletRequest request) {
    log.info(
        ">>>> Method: {}; URI: {}; QueryString: {}; Headers: {}",
        request.method,
        request.requestURI,
        request.queryString,
        headersToString(request))
  }

  private static String headersToString(HttpServletRequest request) {
    def headers = request.headerNames
        .toList()
        .findAll { it && it.trim() }
        .collect { headerName ->
          def headerValue = request.getHeader(headerName)
          if (headerValue && headerValue.trim()) {
            "${headerName}: ${headerValue}"
          } else {
            null
          }
        }
        .findAll { it != null }

    if (headers.isEmpty()) {
      "{}"
    } else {
      headers.join(", ").with { "{${it}}" }
    }
  }

  private static void logResponseDetails(HttpServletResponse response) {
    log.info("<<<< Response Status: {}", response.status)
  }
}
