package dev.pollito.spring_java.config.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.core.userdetails.User.withUsername;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

  private final JwtProperties jwtProperties =
      new JwtProperties("this-is-a-test-secret-key-make-it-long", 3600000);
  private final JwtService jwtService = new JwtService(jwtProperties);

  @Test
  void generateTokenAndValidate() {
    UserDetails userDetails = withUsername("Mike").password("password").roles("STAFF").build();
    String token = jwtService.generateToken(userDetails);
    assertNotNull(token);
    assertTrue(jwtService.isTokenValid(token, userDetails));
    assertEquals("Mike", jwtService.extractUsername(token));
  }

  @Test
  void invalidTokenFailsValidation() {
    UserDetails userDetails = withUsername("Mike").password("password").roles("STAFF").build();
    String token = jwtService.generateToken(userDetails);
    UserDetails otherUser = withUsername("Jon").password("password").roles("STAFF").build();
    assertFalse(jwtService.isTokenValid(token, otherUser));
  }

  @Test
  void expiredTokenFailsValidation() {
    JwtProperties expiredProperties =
        new JwtProperties("this-is-a-test-secret-key-make-it-long", -1);
    JwtService expiredJwtService = new JwtService(expiredProperties);
    UserDetails userDetails = withUsername("Mike").password("password").roles("STAFF").build();
    String token = expiredJwtService.generateToken(userDetails);
    assertFalse(expiredJwtService.isTokenValid(token, userDetails));
  }
}
