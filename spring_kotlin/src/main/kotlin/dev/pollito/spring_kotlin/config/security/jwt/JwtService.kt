package dev.pollito.spring_kotlin.config.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts.builder
import io.jsonwebtoken.Jwts.parser
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import java.lang.System.currentTimeMillis
import java.nio.charset.StandardCharsets.UTF_8
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class JwtService(private val jwtProperties: JwtProperties) {

  fun generateToken(userDetails: UserDetails): String {
    return builder()
        .subject(userDetails.username)
        .issuedAt(Date(currentTimeMillis()))
        .expiration(Date(currentTimeMillis() + jwtProperties.expirationMs))
        .signWith(signInKey())
        .compact()
  }

  fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
    return extractUsername(token) == userDetails.username && !isTokenExpired(token)
  }

  fun extractUsername(token: String): String? {
    return extractClaim(token, Claims::getSubject)
  }

  fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T? {
    return try {
      claimsResolver(extractAllClaims(token))
    } catch (e: ExpiredJwtException) {
      claimsResolver(e.claims)
    }
  }

  private fun isTokenExpired(token: String): Boolean {
    return extractClaim(token, Claims::getExpiration)?.before(Date()) ?: true
  }

  private fun extractAllClaims(token: String): Claims {
    return parser().verifyWith(signInKey()).build().parseSignedClaims(token).payload
  }

  private fun signInKey(): SecretKey {
    return hmacShaKeyFor(jwtProperties.secret.toByteArray(UTF_8))
  }
}
