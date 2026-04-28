package dev.pollito.spring_kotlin.sakila.auth.domain.service

import dev.pollito.spring_kotlin.config.security.jwt.JwtService
import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_kotlin.sakila.auth.domain.port.`in`.AuthUseCases
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthUseCasesImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
) : AuthUseCases {

  override fun authenticate(username: String, password: String): String {
    val authentication =
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        )
    val userDetails = authentication.principal as SakilaUserDetails
    return jwtService.generateToken(userDetails)
  }

  override fun getCurrentUser(): SakilaUserDetails {
    val authentication = SecurityContextHolder.getContext().authentication
    require(authentication != null && authentication.principal is SakilaUserDetails) {
      "No authenticated user found"
    }
    return authentication.principal as SakilaUserDetails
  }
}
