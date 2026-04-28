package dev.pollito.spring_kotlin.config.security

import dev.pollito.spring_kotlin.config.security.handler.CustomAccessDeniedHandler
import dev.pollito.spring_kotlin.config.security.handler.CustomAuthenticationEntryPoint
import dev.pollito.spring_kotlin.config.security.jwt.JwtAuthenticationFilter
import dev.pollito.spring_kotlin.config.security.jwt.JwtProperties
import dev.pollito.spring_kotlin.config.security.jwt.JwtService
import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetailsService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val sakilaUserDetailsService: SakilaUserDetailsService,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
) {

  @Bean
  fun securityFilterChain(
      http: HttpSecurity,
      jwtAuthenticationFilter: JwtAuthenticationFilter,
  ): SecurityFilterChain {
    http
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests { auth ->
          auth
              .requestMatchers("/actuator/**")
              .permitAll()
              .requestMatchers("/h2-console/**")
              .permitAll()
              .requestMatchers(HttpMethod.GET, "/api/films/**")
              .permitAll()
              .requestMatchers("/api/auth/login")
              .permitAll()
              .anyRequest()
              .authenticated()
        }
        .exceptionHandling {
          it.authenticationEntryPoint(authenticationEntryPoint)
              .accessDeniedHandler(accessDeniedHandler)
        }
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    return http.build()
  }

  @Bean
  fun jwtAuthenticationFilter(
      jwtService: JwtService,
      userDetailsService: UserDetailsService,
  ): JwtAuthenticationFilter {
    return JwtAuthenticationFilter(jwtService, userDetailsService)
  }

  @Bean
  fun authenticationProvider(): AuthenticationProvider {
    val authProvider = DaoAuthenticationProvider(sakilaUserDetailsService)
    authProvider.setPasswordEncoder(passwordEncoder())
    return authProvider
  }

  @Bean
  fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
    return config.authenticationManager
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }
}
