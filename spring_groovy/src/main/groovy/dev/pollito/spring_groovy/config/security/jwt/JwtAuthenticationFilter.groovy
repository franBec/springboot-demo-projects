package dev.pollito.spring_groovy.config.security.jwt

import groovy.transform.CompileStatic
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

@CompileStatic
class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService
  private final UserDetailsService userDetailsService

  JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService
    this.userDetailsService = userDetailsService
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    String authHeader = request.getHeader("Authorization")
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    String token = authHeader.substring(7)
    String username = jwtService.extractUsername(token)

    if (username == null || SecurityContextHolder.context.authentication != null) {
      filterChain.doFilter(request, response)
      return
    }

    def userDetails = userDetailsService.loadUserByUsername(username)
    if (!jwtService.isTokenValid(token, userDetails)) {
      filterChain.doFilter(request, response)
      return
    }

    def authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    authToken.details = new WebAuthenticationDetailsSource().buildDetails(request)
    SecurityContextHolder.context.authentication = authToken

    filterChain.doFilter(request, response)
  }
}
