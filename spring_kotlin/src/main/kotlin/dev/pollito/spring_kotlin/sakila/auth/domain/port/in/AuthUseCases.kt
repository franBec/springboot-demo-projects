package dev.pollito.spring_kotlin.sakila.auth.domain.port.`in`

import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetails

interface AuthUseCases {
  fun authenticate(username: String, password: String): String

  fun getCurrentUser(): SakilaUserDetails
}
