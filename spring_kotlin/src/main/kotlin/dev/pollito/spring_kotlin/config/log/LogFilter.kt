package dev.pollito.spring_kotlin.config.log

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private val log = KotlinLogging.logger {}

@Component
@Order(LOWEST_PRECEDENCE)
class LogFilter : OncePerRequestFilter() {

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
  ) {
    logRequestDetails(request)
    filterChain.doFilter(request, response)
    logResponseDetails(response)
  }

  private fun logRequestDetails(request: HttpServletRequest) {
    log.info {
      ">>>> Method: ${request.method}; URI: ${request.requestURI}; QueryString: ${request.queryString}; Headers: ${headersToString(request)}"
    }
  }

  private fun headersToString(request: HttpServletRequest): String {
    val headers =
        request.headerNames
            .toList()
            .filter { !it.isNullOrBlank() }
            .mapNotNull { headerName ->
              val headerValue = request.getHeader(headerName)
              if (!headerValue.isNullOrBlank()) {
                "$headerName: $headerValue"
              } else {
                null
              }
            }

    return if (headers.isEmpty()) {
      "{}"
    } else {
      headers.joinToString(separator = ", ", prefix = "{", postfix = "}")
    }
  }

  private fun logResponseDetails(response: HttpServletResponse) {
    log.info { "<<<< Response Status: ${response.status}" }
  }
}
