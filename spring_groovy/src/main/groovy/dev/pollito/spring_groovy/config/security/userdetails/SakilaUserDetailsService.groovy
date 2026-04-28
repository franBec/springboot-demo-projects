package dev.pollito.spring_groovy.config.security.userdetails

import dev.pollito.spring_groovy.sakila.staff.domain.port.out.StaffRepository
import groovy.transform.CompileStatic
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@CompileStatic
class SakilaUserDetailsService implements UserDetailsService {
  private final StaffRepository staffRepository

  SakilaUserDetailsService(StaffRepository staffRepository) {
    this.staffRepository = staffRepository
  }

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    staffRepository.findByUsername(username)
        .map { new SakilaUserDetails(it) }
        .orElseThrow { new UsernameNotFoundException(username) }
  }
}
