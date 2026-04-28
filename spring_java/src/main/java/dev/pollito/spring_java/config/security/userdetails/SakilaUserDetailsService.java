package dev.pollito.spring_java.config.security.userdetails;

import dev.pollito.spring_java.sakila.staff.domain.port.out.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SakilaUserDetailsService implements UserDetailsService {

  private final StaffRepository staffRepository;

  @Override
  public @NonNull UserDetails loadUserByUsername(@NonNull String username)
      throws UsernameNotFoundException {
    // Composite lookup: tries staff first, ready for future customer fallback.
    // Example: .or(() -> customerRepository.findByUsername(username).map(SakilaUserDetails::new))
    // Both staff and customers will share /api/auth/login.
    return staffRepository
        .findByUsername(username)
        .map(SakilaUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
