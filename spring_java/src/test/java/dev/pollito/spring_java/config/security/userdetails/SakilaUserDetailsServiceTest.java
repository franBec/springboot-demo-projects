package dev.pollito.spring_java.config.security.userdetails;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import dev.pollito.spring_java.sakila.staff.domain.port.out.StaffRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class SakilaUserDetailsServiceTest {

  @Mock private StaffRepository staffRepository;

  @InjectMocks private SakilaUserDetailsService sakilaUserDetailsService;

  @Test
  void loadUserByUsername() {
    Staff staff =
        Staff.builder()
            .id(1)
            .username("Mike")
            .password("encoded")
            .firstName("Mike")
            .lastName("Hillyer")
            .active(true)
            .build();
    when(staffRepository.findByUsername("Mike")).thenReturn(Optional.of(staff));

    SakilaUserDetails userDetails =
        (SakilaUserDetails) sakilaUserDetailsService.loadUserByUsername("Mike");

    assertEquals("Mike", userDetails.getUsername());
    assertEquals("encoded", userDetails.getPassword());
    assertTrue(userDetails.isAccountNonLocked());
    assertEquals(staff, userDetails.staff());
  }

  @Test
  void loadUserByUsernameNotFound() {
    when(staffRepository.findByUsername("Unknown")).thenReturn(empty());
    assertThrows(
        UsernameNotFoundException.class,
        () -> sakilaUserDetailsService.loadUserByUsername("Unknown"));
  }
}
