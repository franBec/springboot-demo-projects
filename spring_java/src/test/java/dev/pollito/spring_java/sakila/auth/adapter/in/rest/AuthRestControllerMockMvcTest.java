package dev.pollito.spring_java.sakila.auth.adapter.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.pollito.spring_java.config.security.userdetails.SakilaUserDetails;
import dev.pollito.spring_java.sakila.auth.domain.port.in.AuthUseCases;
import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({AuthRestMapperImpl.class})
class AuthRestControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthUseCases authUseCases;

  @MockitoSpyBean private AuthRestMapper authRestMapper;

  @MockitoBean private UserDetailsService userDetailsService;

  @Test
  void loginReturnsToken() throws Exception {
    when(authUseCases.authenticate(any(), any())).thenReturn("jwt-token-123");

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content("{\"username\":\"Mike\",\"password\":\"password\"}")
                .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.token").value("jwt-token-123"));
  }

  static @NonNull Stream<SakilaUserDetails> getCurrentUserDetailsReturnsUserDetailsArgs() {
    return Stream.of(
        new SakilaUserDetails(
            Staff.builder().id(1).firstName("Mike").active(true).username("Mike").build()),
        null);
  }

  @ParameterizedTest
  @MethodSource("getCurrentUserDetailsReturnsUserDetailsArgs")
  void getCurrentUserDetailsReturnsUserDetails(SakilaUserDetails sakilaUserDetails)
      throws Exception {
    when(authUseCases.getCurrentUser()).thenReturn(sakilaUserDetails);

    var resultActions =
        mockMvc.perform(get("/api/auth/me").accept(APPLICATION_JSON)).andExpect(status().isOk());

    if (sakilaUserDetails != null) {
      resultActions
          .andExpect(jsonPath("$.data.username").value("Mike"))
          .andExpect(jsonPath("$.data.accountNonLocked").value(true))
          .andExpect(jsonPath("$.data.authorities[0]").value("ROLE_STAFF"))
          .andExpect(jsonPath("$.data.staff.username").value("Mike"));
    }
  }
}
