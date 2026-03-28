package dev.pollito.spring_kotlin.config.web

import dev.pollito.spring_kotlin.test.util.hasErrorFields
import dev.pollito.spring_kotlin.test.util.hasStandardApiResponseFields
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import java.util.stream.Stream
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.core.MethodParameter
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.resource.NoResourceFoundException

class ControllerAdviceMockMvcTest {

  private lateinit var mockMvc: MockMvc
  private val request = mockk<HttpServletRequest>()

  @RestController
  @RequestMapping("/fake")
  class FakeController {

    @GetMapping("/not-found")
    fun throwNoResourceFoundException() {
      throw NoResourceFoundException(GET, "/fake/not-found", "no-resource-found")
    }

    @GetMapping("/error")
    fun throwException() {
      throw Exception("Test exception")
    }

    @GetMapping("/bad-request")
    fun throwConstraintViolationException() {
      throw ConstraintViolationException("Constraint violation", emptySet())
    }

    @GetMapping("/method-arg-not-valid")
    fun throwMethodArgumentNotValidException() {
      throw MethodArgumentNotValidException(
          MethodParameter(
              FakeController::class.java.getMethod("throwMethodArgumentNotValidException"),
              -1,
          ),
          mockk<BindingResult>(relaxed = true),
      )
    }
  }

  companion object {
    @JvmStatic
    fun testCases(): Stream<Arguments> =
        Stream.of(
            Arguments.of("/fake/not-found", NOT_FOUND),
            Arguments.of("/fake/error", INTERNAL_SERVER_ERROR),
            Arguments.of("/fake/bad-request", BAD_REQUEST),
            Arguments.of("/fake/method-arg-not-valid", BAD_REQUEST),
        )
  }

  @BeforeEach
  fun setUp() {
    mockMvc =
        standaloneSetup(FakeController()).setControllerAdvice(ControllerAdvice(request)).build()
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("testCases")
  fun `exception handling returns correct status`(expectedInstance: String, status: HttpStatus) {
    every { request.requestURI } returns expectedInstance

    mockMvc.get(expectedInstance).andExpect {
      status { isEqualTo(status.value()) }
      hasStandardApiResponseFields(expectedInstance, status)
      hasErrorFields(status)
    }
  }
}
