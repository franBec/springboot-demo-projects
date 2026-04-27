package dev.pollito.spring_java.sakila.auth.domain.port.in;

import dev.pollito.spring_java.config.security.userdetails.SakilaUserDetails;

public interface AuthUseCases {
  String authenticate(String username, String password);

  SakilaUserDetails getCurrentUser();
}
