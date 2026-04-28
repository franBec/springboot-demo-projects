package dev.pollito.spring_groovy.sakila.auth.domain.service

import static java.util.Objects.requireNonNull
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated
import static org.springframework.security.core.context.SecurityContextHolder.context

import dev.pollito.spring_groovy.config.security.jwt.JwtService
import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_groovy.sakila.auth.domain.port.in.AuthUseCases
import groovy.transform.CompileStatic
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
@CompileStatic
class AuthUseCasesImpl implements AuthUseCases {
  private final AuthenticationManager authenticationManager
  private final JwtService jwtService

  AuthUseCasesImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager
    this.jwtService = jwtService
  }

  @Override
  String authenticate(String username, String password) {
    jwtService.generateToken(
        (UserDetails) requireNonNull(
        authenticationManager.authenticate(unauthenticated(username, password))
        .principal
        )
        )
  }

  @Override
  SakilaUserDetails getCurrentUser() {
    Authentication authentication = context.authentication
    if (authentication == null || !(authentication.principal instanceof SakilaUserDetails)) {
      throw new IllegalStateException("No authenticated user found")
    }
    (SakilaUserDetails) authentication.principal
  }
}
