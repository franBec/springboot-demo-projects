package dev.pollito.spring_kotlin.sakila.auth.adapter.`in`.rest

import com.ninjasquad.springmockk.MockkBean
import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_kotlin.config.web.ControllerAdvice
import dev.pollito.spring_kotlin.sakila.auth.domain.port.`in`.AuthUseCases
import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import io.mockk.every
import kotlin.test.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthRestController::class)
@Import(ControllerAdvice::class, AuthRestMapperImpl::class)
class AuthRestControllerMockMvcTest {

  @MockkBean private lateinit var authUseCases: AuthUseCases
  @MockkBean private lateinit var userDetailsService: UserDetailsService
  @Autowired private lateinit var mockMvc: MockMvc

  @Test
  fun `login returns token`() {
    every { authUseCases.authenticate("Mike", "1234") } returns "jwt-token"

    mockMvc
        .post("/api/auth/login") {
          contentType = MediaType.APPLICATION_JSON
          content = "{\"username\":\"Mike\",\"password\":\"1234\"}"
        }
        .andExpect {
          status { isOk() }
          jsonPath("$.data.token") { value("jwt-token") }
        }
  }

  @ParameterizedTest
  @CsvSource("Mike,true", "null,false")
  fun `getCurrentUserDetails returns user details`(username: String, active: Boolean) {
    val staff =
        if (username == "null") null
        else
            Staff(
                id = 1,
                firstName = "Mike",
                lastName = "Hillyer",
                username = username,
                password = "password",
                email = "Mike.Hillyer@sakilastaff.com",
                active = active,
            )
    val userDetails = staff?.let { SakilaUserDetails(it) }
    if (userDetails != null) {
      every { authUseCases.getCurrentUser() } returns userDetails

      mockMvc
          .get("/api/auth/me") { accept = MediaType.APPLICATION_JSON }
          .andExpect {
            status { isOk() }
            jsonPath("$.data.username") { value(username) }
            jsonPath("$.data.accountNonLocked") { value(active) }
            jsonPath("$.data.authorities[0]") { value("ROLE_STAFF") }
            jsonPath("$.data.staff.username") { value(username) }
          }
    }
  }
}
