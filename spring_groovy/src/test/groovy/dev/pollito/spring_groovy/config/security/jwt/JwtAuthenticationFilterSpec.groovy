package dev.pollito.spring_groovy.config.security.jwt

import static org.springframework.security.core.context.SecurityContextHolder.context

import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import spock.lang.Specification

class JwtAuthenticationFilterSpec extends Specification {

  def jwtService = Mock(JwtService)
  def userDetailsService = Mock(UserDetailsService)
  def filter = new JwtAuthenticationFilter(jwtService, userDetailsService)

  def request = Mock(HttpServletRequest)
  def response = Mock(HttpServletResponse)
  def filterChain = Mock(FilterChain)

  def setup() {
    SecurityContextHolder.clearContext()
  }

  def cleanup() {
    SecurityContextHolder.clearContext()
  }

  def "no authorization header proceeds chain"() {
    given:
    request.getHeader("Authorization") >> null

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication == null
  }

  def "authorization without Bearer prefix proceeds chain"() {
    given:
    request.getHeader("Authorization") >> "Basic abc"

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication == null
  }

  def "malformed token throws InsufficientAuthenticationException"() {
    given:
    request.getHeader("Authorization") >> "Bearer invalid-token"
    jwtService.extractUsername("invalid-token") >> { throw new MalformedJwtException("bad") }

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    thrown(InsufficientAuthenticationException)
  }

  def "null username from token proceeds chain"() {
    given:
    request.getHeader("Authorization") >> "Bearer token"
    jwtService.extractUsername("token") >> null

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication == null
  }

  def "existing authentication proceeds chain"() {
    given:
    request.getHeader("Authorization") >> "Bearer token"
    jwtService.extractUsername("token") >> "Mike"
    def existingAuth = Mock(Authentication)
    context.authentication = existingAuth

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication.is(existingAuth)
  }

  def "invalid token proceeds chain"() {
    given:
    request.getHeader("Authorization") >> "Bearer token"
    jwtService.extractUsername("token") >> "Mike"
    def userDetails = User.builder().username("Mike").password("password").authorities([]).build()
    userDetailsService.loadUserByUsername("Mike") >> userDetails
    jwtService.isTokenValid("token", userDetails) >> false

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication == null
  }

  def "valid token sets authentication"() {
    given:
    request.getHeader("Authorization") >> "Bearer token"
    jwtService.extractUsername("token") >> "Mike"
    def userDetails = User.builder().username("Mike").password("password").authorities([]).build()
    userDetailsService.loadUserByUsername("Mike") >> userDetails
    jwtService.isTokenValid("token", userDetails) >> true

    when:
    filter.doFilterInternal(request, response, filterChain)

    then:
    1 * filterChain.doFilter(request, response)
    context.authentication != null
    context.authentication.principal == userDetails
    context.authentication.authenticated
  }
}
