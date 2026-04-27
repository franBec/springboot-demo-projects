package dev.pollito.spring_java.sakila.auth.domain.service;

import static java.util.Objects.requireNonNull;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import dev.pollito.spring_java.config.security.jwt.JwtService;
import dev.pollito.spring_java.config.security.userdetails.SakilaUserDetails;
import dev.pollito.spring_java.sakila.auth.domain.port.in.AuthUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCasesImpl implements AuthUseCases {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public String authenticate(String username, String password) {
    return jwtService.generateToken(
        (UserDetails)
            requireNonNull(
                authenticationManager
                    .authenticate(unauthenticated(username, password))
                    .getPrincipal()));
  }

  @Override
  public SakilaUserDetails getCurrentUser() {
    Authentication authentication = getContext().getAuthentication();
    if (authentication == null
        || !(authentication.getPrincipal() instanceof SakilaUserDetails details)) {
      throw new IllegalStateException("No authenticated user found");
    }
    return details;
  }
}
