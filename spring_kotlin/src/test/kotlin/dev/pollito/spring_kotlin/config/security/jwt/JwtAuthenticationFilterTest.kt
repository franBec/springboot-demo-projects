package dev.pollito.spring_kotlin.config.security.jwt

import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder.clearContext
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService

class JwtAuthenticationFilterTest {

  private val jwtService: JwtService = mockk()
  private val userDetailsService: UserDetailsService = mockk()
  private val filter = JwtAuthenticationFilter(jwtService, userDetailsService)

  private fun doFilterInternal(
      filter: JwtAuthenticationFilter,
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
  ) {
    val method =
        JwtAuthenticationFilter::class
            .java
            .getDeclaredMethod(
                "doFilterInternal",
                HttpServletRequest::class.java,
                HttpServletResponse::class.java,
                FilterChain::class.java,
            )
    method.isAccessible = true
    try {
      method.invoke(filter, request, response, filterChain)
    } catch (e: InvocationTargetException) {
      throw e.cause ?: e
    }
  }

  @Test
  fun `no authorization header proceeds chain`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)

    every { request.getHeader("Authorization") } returns null

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }
  }

  @Test
  fun `authorization without Bearer prefix proceeds chain`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)

    every { request.getHeader("Authorization") } returns "Basic abc"

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }
  }

  @Test
  fun `malformed token throws InsufficientAuthenticationException`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)

    every { request.getHeader("Authorization") } returns "Bearer invalid-token"
    every { jwtService.extractUsername("invalid-token") } throws MalformedJwtException("bad")

    assertFailsWith<InsufficientAuthenticationException> {
      doFilterInternal(filter, request, response, filterChain)
    }
  }

  @Test
  fun `null username from token proceeds chain`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)

    every { request.getHeader("Authorization") } returns "Bearer token"
    every { jwtService.extractUsername("token") } returns null

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }
  }

  @Test
  fun `existing authentication proceeds chain`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)

    every { request.getHeader("Authorization") } returns "Bearer token"
    every { jwtService.extractUsername("token") } returns "Mike"

    SecurityContextHolder.getContext().authentication = mockk()

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }

    clearContext()
  }

  @Test
  fun `invalid token proceeds chain`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)
    val userDetails = User("Mike", "password", emptyList())

    every { request.getHeader("Authorization") } returns "Bearer token"
    every { jwtService.extractUsername("token") } returns "Mike"
    every { userDetailsService.loadUserByUsername("Mike") } returns userDetails
    every { jwtService.isTokenValid("token", any()) } returns false

    clearContext()

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }
  }

  @Test
  fun `valid token sets authentication`() {
    val request: HttpServletRequest = mockk(relaxed = true)
    val response: HttpServletResponse = mockk(relaxed = true)
    val filterChain: FilterChain = mockk(relaxed = true)
    val userDetails = User("Mike", "password", emptyList())

    every { request.getHeader("Authorization") } returns "Bearer token"
    every { jwtService.extractUsername("token") } returns "Mike"
    every { userDetailsService.loadUserByUsername("Mike") } returns userDetails
    every { jwtService.isTokenValid("token", any()) } returns true

    clearContext()

    doFilterInternal(filter, request, response, filterChain)

    verify { filterChain.doFilter(request, response) }
    assertNotNull(SecurityContextHolder.getContext().authentication)

    clearContext()
  }
}
