package dev.pollito.spring_groovy.config.security.handler

import groovy.transform.CompileStatic
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
@CompileStatic
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
    AuthenticationErrorResponseWriter.write(request, response, HttpStatus.UNAUTHORIZED, authException.message)
  }
}
