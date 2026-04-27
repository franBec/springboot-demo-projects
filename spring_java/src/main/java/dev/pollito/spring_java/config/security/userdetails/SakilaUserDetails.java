package dev.pollito.spring_java.config.security.userdetails;

import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record SakilaUserDetails(Staff staff) implements UserDetails {

  @Override
  public String getUsername() {
    return staff.getUsername();
  }

  @Override
  public String getPassword() {
    return staff.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_STAFF"));
  }

  @Override
  public boolean isAccountNonLocked() {
    return staff.isActive();
  }
}
