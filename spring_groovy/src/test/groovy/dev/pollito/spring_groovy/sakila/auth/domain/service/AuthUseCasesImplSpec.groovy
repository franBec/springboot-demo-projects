package dev.pollito.spring_groovy.sakila.auth.domain.service

import dev.pollito.spring_groovy.config.security.jwt.JwtService
import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

class AuthUseCasesImplSpec extends Specification {

  def authenticationManager = Mock(AuthenticationManager)
  def jwtService = Mock(JwtService)
  def authUseCases = new AuthUseCasesImpl(authenticationManager, jwtService)

  def setup() {
    SecurityContextHolder.clearContext()
  }

  def cleanup() {
    SecurityContextHolder.clearContext()
  }

  def "authenticate returns token"() {
    given:
    def userDetails = User.builder().username("Mike").password("password").authorities([]).build()
    def authentication = Mock(Authentication)
    authentication.principal >> userDetails
    authenticationManager.authenticate(_ as UsernamePasswordAuthenticationToken) >> authentication
    jwtService.generateToken(userDetails) >> "jwt-token-123"

    expect:
    authUseCases.authenticate("Mike", "password") == "jwt-token-123"
  }

  def "getCurrentUser returns user"() {
    given:
    def staff = new Staff(id: 1, username: "Mike", active: true)
    def userDetails = new SakilaUserDetails(staff)
    def authentication = Mock(Authentication)
    authentication.principal >> userDetails
    SecurityContextHolder.context.authentication = authentication

    when:
    def result = authUseCases.getCurrentUser()

    then:
    result.username == "Mike"
    result.accountNonLocked
    result.staff == staff
  }

  def "getCurrentUser with null authentication throws IllegalStateException"() {
    when:
    authUseCases.getCurrentUser()

    then:
    def ex = thrown(IllegalStateException)
    ex.message == "No authenticated user found"
  }

  def "getCurrentUser with non-SakilaUserDetails principal throws IllegalStateException"() {
    given:
    def authentication = Mock(Authentication)
    authentication.principal >> "not-a-user-details"
    SecurityContextHolder.context.authentication = authentication

    when:
    authUseCases.getCurrentUser()

    then:
    def ex = thrown(IllegalStateException)
    ex.message == "No authenticated user found"
  }
}
