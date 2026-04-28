package dev.pollito.spring_groovy.config.security.jwt

import groovy.transform.CompileStatic
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
@CompileStatic
class JwtProperties {
  String secret
  long expirationMs
}
