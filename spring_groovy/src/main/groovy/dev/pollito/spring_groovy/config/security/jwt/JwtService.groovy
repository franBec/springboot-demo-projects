package dev.pollito.spring_groovy.config.security.jwt

import static io.jsonwebtoken.Jwts.parser
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor
import static java.nio.charset.StandardCharsets.UTF_8

import groovy.transform.CompileStatic
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import java.util.Date
import javax.crypto.SecretKey
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
@CompileStatic
class JwtService {
  private final JwtProperties jwtProperties

  JwtService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties
  }

  String generateToken(UserDetails userDetails) {
    Jwts.builder()
        .subject(userDetails.username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs))
        .signWith(signInKey())
        .compact()
  }

  boolean isTokenValid(String token, UserDetails userDetails) {
    extractUsername(token) == userDetails.username && !isTokenExpired(token)
  }

  String extractUsername(String token) {
    extractClaim(token, { Claims c -> c.subject })
  }

  private boolean isTokenExpired(String token) {
    Date expiration = extractClaim(token, { Claims c -> c.expiration })
    expiration != null ? expiration.before(new Date()) : true
  }

  private <T> T extractClaim(String token, Closure<T> claimsResolver) {
    try {
      claimsResolver.call(extractAllClaims(token))
    } catch (ExpiredJwtException e) {
      claimsResolver.call(e.claims)
    }
  }

  private Claims extractAllClaims(String token) {
    parser().verifyWith(signInKey()).build().parseSignedClaims(token).payload
  }

  private SecretKey signInKey() {
    hmacShaKeyFor(jwtProperties.secret.getBytes(UTF_8))
  }
}
