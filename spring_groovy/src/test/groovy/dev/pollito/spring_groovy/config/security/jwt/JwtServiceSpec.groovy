package dev.pollito.spring_groovy.config.security.jwt

import static io.jsonwebtoken.Jwts.builder
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.security.core.userdetails.User.withUsername

import spock.lang.Specification

class JwtServiceSpec extends Specification {

  private static final String SECRET = "this-is-a-test-secret-key-make-it-long"
  private static final long EXPIRATION_MS = 3600000

  private JwtProperties jwtProperties = new JwtProperties(secret: SECRET, expirationMs: EXPIRATION_MS)
  private JwtService jwtService = new JwtService(jwtProperties)

  def "generateTokenAndValidate"() {
    given:
    def userDetails = withUsername("Mike").password("password").roles("STAFF").build()

    when:
    def token = jwtService.generateToken(userDetails)

    then:
    token != null
    jwtService.isTokenValid(token, userDetails)
    jwtService.extractUsername(token) == "Mike"
  }

  def "invalidTokenFailsValidation"() {
    given:
    def userDetails = withUsername("Mike").password("password").roles("STAFF").build()
    def token = jwtService.generateToken(userDetails)
    def otherUser = withUsername("Jon").password("password").roles("STAFF").build()

    expect:
    !jwtService.isTokenValid(token, otherUser)
  }

  def "expiredTokenFailsValidation"() {
    given:
    def expiredProperties = new JwtProperties(secret: SECRET, expirationMs: -1)
    def expiredJwtService = new JwtService(expiredProperties)
    def userDetails = withUsername("Mike").password("password").roles("STAFF").build()
    def token = expiredJwtService.generateToken(userDetails)

    expect:
    !expiredJwtService.isTokenValid(token, userDetails)
  }

  def "tokenWithoutExpirationIsConsideredExpired"() {
    given:
    def userDetails = withUsername("Mike").password("password").roles("STAFF").build()
    def token = builder()
        .subject(userDetails.username)
        .signWith(hmacShaKeyFor(SECRET.getBytes(UTF_8)))
        .compact()

    expect:
    !jwtService.isTokenValid(token, userDetails)
  }
}
