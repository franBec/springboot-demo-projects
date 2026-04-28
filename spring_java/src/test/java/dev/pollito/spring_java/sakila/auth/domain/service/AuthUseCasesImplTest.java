package dev.pollito.spring_java.sakila.auth.domain.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import dev.pollito.spring_java.config.security.jwt.JwtService;
import dev.pollito.spring_java.config.security.userdetails.SakilaUserDetails;
import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class AuthUseCasesImplTest {

  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;
  @InjectMocks private AuthUseCasesImpl authUseCases;

  @BeforeEach
  void setUp() {
    clearContext();
  }

  @AfterEach
  void tearDown() {
    clearContext();
  }

  @Test
  void authenticateReturnsToken() {
    UserDetails userDetails =
        User.builder().username("Mike").password("password").authorities(emptyList()).build();
    String token = "jwt-token-123";
    Authentication authentication = mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(jwtService.generateToken(userDetails)).thenReturn(token);

    assertEquals(token, authUseCases.authenticate("Mike", "password"));
  }

  @Test
  void getCurrentUserReturnsUser() {
    Staff staff = Staff.builder().id(1).username("Mike").active(true).build();
    SakilaUserDetails userDetails = new SakilaUserDetails(staff);
    Authentication authentication = mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(userDetails);
    getContext().setAuthentication(authentication);

    SakilaUserDetails result = authUseCases.getCurrentUser();

    assertEquals("Mike", result.getUsername());
    assertTrue(result.isAccountNonLocked());
    assertEquals(staff, result.staff());
  }

  private static @NonNull Stream<Arguments> provideInvalidAuthentications() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn("not-a-user-details");
    return Stream.of(Arguments.of((Authentication) null), Arguments.of(authentication));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidAuthentications")
  void getCurrentUserInvalidAuthenticationThrowsIllegalStateException(
      Authentication authentication) {
    getContext().setAuthentication(authentication);

    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> authUseCases.getCurrentUser());

    assertEquals("No authenticated user found", ex.getMessage());
  }
}
