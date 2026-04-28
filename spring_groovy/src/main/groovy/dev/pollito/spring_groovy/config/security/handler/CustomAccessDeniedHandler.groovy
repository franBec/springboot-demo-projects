package dev.pollito.spring_groovy.config.security.handler

import groovy.transform.CompileStatic
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
@CompileStatic
class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
    AuthenticationErrorResponseWriter.write(request, response, HttpStatus.FORBIDDEN, accessDeniedException.message)
  }
}
