package dev.pollito.spring_kotlin.config.security.userdetails

import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import dev.pollito.spring_kotlin.sakila.staff.domain.port.out.StaffRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UsernameNotFoundException

@ExtendWith(MockKExtension::class)
class SakilaUserDetailsServiceTest {

  @MockK private lateinit var staffRepository: StaffRepository

  @InjectMockKs private lateinit var sakilaUserDetailsService: SakilaUserDetailsService

  companion object {
    private val STAFF =
        Staff(
            id = 1,
            firstName = "Mike",
            lastName = "Hillyer",
            username = "Mike",
            password = "password",
            email = "Mike.Hillyer@sakilastaff.com",
            active = true,
        )
  }

  @Test
  fun `loadUserByUsername returns user details`() {
    every { staffRepository.findByUsername("Mike") } returns Optional.of(STAFF)

    val result = sakilaUserDetailsService.loadUserByUsername("Mike")

    assertEquals("Mike", result.username)
    assertEquals("password", result.password)
    assertTrue(result.isAccountNonLocked)
    assertEquals(STAFF, (result as SakilaUserDetails).staff)
    verify { staffRepository.findByUsername("Mike") }
  }

  @Test
  fun `loadUserByUsername throws when staff not found`() {
    every { staffRepository.findByUsername("Unknown") } returns Optional.empty()

    assertFailsWith<UsernameNotFoundException> {
      sakilaUserDetailsService.loadUserByUsername("Unknown")
    }
  }
}
