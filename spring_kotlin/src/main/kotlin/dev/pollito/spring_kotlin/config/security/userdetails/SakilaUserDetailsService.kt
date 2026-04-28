package dev.pollito.spring_kotlin.config.security.userdetails

import dev.pollito.spring_kotlin.sakila.staff.domain.port.out.StaffRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SakilaUserDetailsService(private val staffRepository: StaffRepository) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    // Composite lookup: tries staff first, ready for future customer fallback.
    // Example: .or { customerRepository.findByUsername(username).map(::SakilaUserDetails) }
    // Both staff and customers will share /api/auth/login.
    return staffRepository.findByUsername(username).map(::SakilaUserDetails).orElseThrow {
      UsernameNotFoundException(username)
    }
  }
}
