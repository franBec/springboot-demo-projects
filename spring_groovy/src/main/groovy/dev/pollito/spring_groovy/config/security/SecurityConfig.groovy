package dev.pollito.spring_groovy.config.security

import static org.springframework.http.HttpMethod.GET
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS

import dev.pollito.spring_groovy.config.security.handler.CustomAccessDeniedHandler
import dev.pollito.spring_groovy.config.security.handler.CustomAuthenticationEntryPoint
import dev.pollito.spring_groovy.config.security.jwt.JwtAuthenticationFilter
import dev.pollito.spring_groovy.config.security.jwt.JwtProperties
import dev.pollito.spring_groovy.config.security.jwt.JwtService
import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetailsService
import groovy.transform.CompileStatic
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties)
@CompileStatic
class SecurityConfig {
  private final SakilaUserDetailsService sakilaUserDetailsService
  private final CustomAuthenticationEntryPoint authenticationEntryPoint
  private final CustomAccessDeniedHandler accessDeniedHandler

  SecurityConfig(SakilaUserDetailsService sakilaUserDetailsService,
  CustomAuthenticationEntryPoint authenticationEntryPoint,
  CustomAccessDeniedHandler accessDeniedHandler) {
    this.sakilaUserDetailsService = sakilaUserDetailsService
    this.authenticationEntryPoint = authenticationEntryPoint
    this.accessDeniedHandler = accessDeniedHandler
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
    http
        .csrf { AbstractHttpConfigurer csrf -> csrf.disable() }
        .sessionManagement { it.sessionCreationPolicy(STATELESS) }
        .authorizeHttpRequests { auth ->
          auth
              .requestMatchers("/actuator/**").permitAll()
              .requestMatchers("/h2-console/**").permitAll()
              .requestMatchers("/error").permitAll()
              .requestMatchers(GET, "/api/films/**").permitAll()
              .requestMatchers("/api/auth/login").permitAll()
              .anyRequest().authenticated()
        }
        .exceptionHandling {
          it.authenticationEntryPoint(authenticationEntryPoint)
              .accessDeniedHandler(accessDeniedHandler)
        }
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter)
    http.build()
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    new JwtAuthenticationFilter(jwtService, userDetailsService)
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    def authProvider = new DaoAuthenticationProvider(sakilaUserDetailsService)
    authProvider.setPasswordEncoder(passwordEncoder())
    authProvider
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    config.authenticationManager
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    new BCryptPasswordEncoder()
  }
}
