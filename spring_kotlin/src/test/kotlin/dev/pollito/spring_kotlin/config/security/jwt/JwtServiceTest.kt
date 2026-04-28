package dev.pollito.spring_kotlin.config.security.jwt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.springframework.security.core.userdetails.User

class JwtServiceTest {

  private val jwtProperties = JwtProperties(secret = TEST_SECRET, expirationMs = 3600000)
  private val jwtService = JwtService(jwtProperties)

  companion object {
    private const val TEST_SECRET =
        "test-secret-key-that-is-long-enough-for-HS256-algorithm-1234567890abcdef"
  }

  @Test
  fun `generateToken and validate success`() {
    val userDetails = User("Mike", "password", emptyList())
    val token = jwtService.generateToken(userDetails)
    assertNotNull(token)
    assertTrue(jwtService.isTokenValid(token, userDetails))
    assertEquals("Mike", jwtService.extractUsername(token))
  }

  @Test
  fun `token fails validation for different user`() {
    val userDetails = User("Mike", "password", emptyList())
    val otherUser = User("Jon", "password", emptyList())
    val token = jwtService.generateToken(userDetails)
    assertFalse(jwtService.isTokenValid(token, otherUser))
  }

  @Test
  fun `expired token fails validation`() {
    val expiredProperties = JwtProperties(secret = TEST_SECRET, expirationMs = -1)
    val expiredJwtService = JwtService(expiredProperties)
    val userDetails = User("Mike", "password", emptyList())
    val token = expiredJwtService.generateToken(userDetails)
    assertFalse(expiredJwtService.isTokenValid(token, userDetails))
  }

  @Test
  fun `token without expiration claim is considered expired`() {
    val tokenWithoutExp =
        io.jsonwebtoken.Jwts.builder()
            .subject("Mike")
            .signWith(
                io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                    TEST_SECRET.toByteArray(Charsets.UTF_8)))
            .compact()
    val userDetails = User("Mike", "password", emptyList())
    assertFalse(jwtService.isTokenValid(tokenWithoutExp, userDetails))
  }
}
