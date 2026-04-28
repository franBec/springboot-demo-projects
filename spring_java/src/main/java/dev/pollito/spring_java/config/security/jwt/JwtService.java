package dev.pollito.spring_java.config.security.jwt;

import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final JwtProperties jwtProperties;

  public JwtService(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
  }

  public String generateToken(@NonNull UserDetails userDetails) {
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
        .signWith(getSignInKey())
        .compact();
  }

  public boolean isTokenValid(String token, @NonNull UserDetails userDetails) {
    return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private <T> T extractClaim(String token, @NonNull Function<Claims, T> claimsResolver) {
    try {
      return claimsResolver.apply(
          parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload());
    } catch (ExpiredJwtException e) {
      return claimsResolver.apply(e.getClaims());
    }
  }

  @Contract(" -> new")
  private @NonNull SecretKey getSignInKey() {
    return hmacShaKeyFor(jwtProperties.secret().getBytes(UTF_8));
  }
}
