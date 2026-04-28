package dev.pollito.spring_kotlin.sakila.auth.domain.service

import dev.pollito.spring_kotlin.config.security.jwt.JwtService
import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder.clearContext
import org.springframework.security.core.context.SecurityContextHolder.getContext

@ExtendWith(MockKExtension::class)
class AuthUseCasesImplTest {

  @MockK private lateinit var authenticationManager: AuthenticationManager
  @MockK private lateinit var jwtService: JwtService
  @InjectMockKs private lateinit var authUseCases: AuthUseCasesImpl

  companion object {
    private val SAKILA_USER_DETAILS =
        SakilaUserDetails(
            Staff(
                id = 1,
                firstName = "Mike",
                lastName = "Hillyer",
                username = "Mike",
                password = "password",
                email = "Mike.Hillyer@sakilastaff.com",
                active = true,
            )
        )
  }

  @Test
  fun `authenticate returns token`() {
    val authentication = UsernamePasswordAuthenticationToken(SAKILA_USER_DETAILS, null, emptyList())
    every { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns
        authentication
    every { jwtService.generateToken(SAKILA_USER_DETAILS) } returns "jwt-token"

    val result = authUseCases.authenticate("Mike", "1234")

    assertEquals("jwt-token", result)
    verify { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) }
    verify { jwtService.generateToken(SAKILA_USER_DETAILS) }
  }

  @Test
  fun `getCurrentUser returns user details`() {
    val authentication = UsernamePasswordAuthenticationToken(SAKILA_USER_DETAILS, null, emptyList())
    getContext().authentication = authentication

    val result = authUseCases.getCurrentUser()

    assertEquals(SAKILA_USER_DETAILS, result)

    clearContext()
  }

  @ParameterizedTest
  @CsvSource("null", "not_sakila_user")
  fun `getCurrentUser throws when no authenticated user`(principalType: String) {
    val authentication = mockk<org.springframework.security.core.Authentication>()
    every { authentication.principal } returns
        if (principalType == "null") null else "not_a_user_details"
    getContext().authentication = authentication

    assertFailsWith<IllegalArgumentException> { authUseCases.getCurrentUser() }

    clearContext()
  }
}
