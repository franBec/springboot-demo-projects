package dev.pollito.spring_kotlin.config.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

  override fun handle(
      request: HttpServletRequest,
      response: HttpServletResponse,
      accessDeniedException: AccessDeniedException,
  ) {
    AuthenticationErrorResponseWriter.write(
        request,
        response,
        FORBIDDEN,
        accessDeniedException.message ?: "Forbidden",
    )
  }
}
