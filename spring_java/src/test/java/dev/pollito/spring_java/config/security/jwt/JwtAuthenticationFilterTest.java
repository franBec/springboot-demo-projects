package dev.pollito.spring_java.config.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock private JwtService jwtService;
  @Mock private UserDetailsService userDetailsService;
  @Mock private HttpServletRequest request;
  @Mock private HttpServletResponse response;
  @Mock private FilterChain filterChain;
  @InjectMocks private JwtAuthenticationFilter filter;

  @BeforeEach
  void setUp() {
    clearContext();
  }

  @AfterEach
  void tearDown() {
    clearContext();
  }

  @Test
  void doFilterInternal_noAuthHeader_proceedsChain() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn(null);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_authHeaderWithoutBearer_proceedsChain()
      throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_malformedToken_throwsInsufficientAuthenticationException() {
    when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
    when(jwtService.extractUsername("invalid-token")).thenThrow(new MalformedJwtException("bad"));

    assertThrows(
        InsufficientAuthenticationException.class,
        () -> filter.doFilterInternal(request, response, filterChain));
  }

  @Test
  void doFilterInternal_nullUsername_proceedsChain() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUsername("token")).thenReturn(null);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_existingAuthentication_proceedsChain()
      throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUsername("token")).thenReturn("Mike");

    Authentication existingAuth = mock(Authentication.class);
    getContext().setAuthentication(existingAuth);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertSame(existingAuth, getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_invalidToken_proceedsChain() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUsername("token")).thenReturn("Mike");

    UserDetails userDetails =
        User.builder()
            .username("Mike")
            .password("password")
            .authorities(Collections.emptyList())
            .build();
    when(userDetailsService.loadUserByUsername("Mike")).thenReturn(userDetails);
    when(jwtService.isTokenValid("token", userDetails)).thenReturn(false);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(getContext().getAuthentication());
  }

  @Test
  void doFilterInternal_validToken_setsAuthenticationAndProceedsChain()
      throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtService.extractUsername("token")).thenReturn("Mike");

    UserDetails userDetails =
        User.builder()
            .username("Mike")
            .password("password")
            .authorities(Collections.emptyList())
            .build();
    when(userDetailsService.loadUserByUsername("Mike")).thenReturn(userDetails);
    when(jwtService.isTokenValid("token", userDetails)).thenReturn(true);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    Authentication auth = getContext().getAuthentication();
    assertNotNull(auth);
    assertEquals(userDetails, auth.getPrincipal());
    assertTrue(auth.isAuthenticated());
  }
}
