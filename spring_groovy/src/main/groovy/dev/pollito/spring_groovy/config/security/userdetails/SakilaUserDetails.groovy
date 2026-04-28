package dev.pollito.spring_groovy.config.security.userdetails

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
class SakilaUserDetails implements UserDetails {
  final Staff staff

  SakilaUserDetails(Staff staff) {
    this.staff = staff
  }

  @Override
  String getUsername() {
    staff.username
  }

  @Override
  String getPassword() {
    staff.password
  }

  @Override
  Collection<? extends GrantedAuthority> getAuthorities() {
    [
      new SimpleGrantedAuthority("ROLE_STAFF")
    ]
  }

  @Override
  boolean isAccountNonLocked() {
    staff.active
  }
}
