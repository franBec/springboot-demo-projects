package dev.pollito.spring_kotlin.config.security.jwt

import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {

  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
  ) {
    val authHeader = request.getHeader("Authorization")
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response)
      return
    }

    val token = authHeader.substring(7)
    val username =
        try {
          jwtService.extractUsername(token)
        } catch (e: JwtException) {
          throw InsufficientAuthenticationException("Invalid or malformed JWT token", e)
        }

    if (username == null || SecurityContextHolder.getContext().authentication != null) {
      filterChain.doFilter(request, response)
      return
    }

    val userDetails = userDetailsService.loadUserByUsername(username)
    if (!jwtService.isTokenValid(token, userDetails)) {
      filterChain.doFilter(request, response)
      return
    }

    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
    SecurityContextHolder.getContext().authentication = authToken

    filterChain.doFilter(request, response)
  }
}
