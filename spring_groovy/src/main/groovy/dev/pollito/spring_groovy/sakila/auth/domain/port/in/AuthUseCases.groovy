package dev.pollito.spring_groovy.sakila.auth.domain.port.in

import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetails
import groovy.transform.CompileStatic

@CompileStatic
interface AuthUseCases {
  String authenticate(String username, String password)

  SakilaUserDetails getCurrentUser()
}
