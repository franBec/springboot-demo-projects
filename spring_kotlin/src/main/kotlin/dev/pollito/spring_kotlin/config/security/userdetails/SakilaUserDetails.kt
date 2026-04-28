package dev.pollito.spring_kotlin.config.security.userdetails

import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SakilaUserDetails(val staff: Staff) : UserDetails {

  override fun getAuthorities(): Collection<GrantedAuthority> {
    return listOf(SimpleGrantedAuthority("ROLE_STAFF"))
  }

  override fun getPassword(): String {
    return staff.password
  }

  override fun getUsername(): String {
    return staff.username
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isAccountNonLocked(): Boolean {
    return staff.active
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return true
  }
}
