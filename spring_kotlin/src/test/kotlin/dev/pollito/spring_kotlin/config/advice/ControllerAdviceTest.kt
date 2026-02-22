package dev.pollito.spring_kotlin.config.advice

import dev.pollito.spring_kotlin.test.util.hasErrorFields
import dev.pollito.spring_kotlin.test.util.hasStandardApiResponseFields
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import kotlin.test.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.resource.NoResourceFoundException

class ControllerAdviceTest {

  private lateinit var mockMvc: MockMvc
  private val request: HttpServletRequest = mockk(relaxed = true)

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
  }

  @BeforeEach
  fun setUp() {
    mockMvc =
        standaloneSetup(FakeController()).setControllerAdvice(ControllerAdvice(request)).build()
  }

  @Test
  fun `when NoResourceFoundException then returns NotFound`() {
    val status = NOT_FOUND
    val expectedInstance = "/fake/not-found"

    every { request.requestURI } returns expectedInstance

    mockMvc.get(expectedInstance).andExpect {
      status { isNotFound() }
      hasStandardApiResponseFields(expectedInstance, status)
      hasErrorFields(status)
    }
  }

  @Test
  fun `when Exception then returns InternalServerError`() {
    val status = INTERNAL_SERVER_ERROR
    val expectedInstance = "/fake/error"

    every { request.requestURI } returns expectedInstance

    mockMvc.get(expectedInstance).andExpect {
      status { isInternalServerError() }
      hasStandardApiResponseFields(expectedInstance, status)
      hasErrorFields(status)
    }
  }

  @Test
  fun `when ConstraintViolationException then returns BadRequest`() {
    val status = BAD_REQUEST
    val expectedInstance = "/fake/bad-request"

    every { request.requestURI } returns expectedInstance

    mockMvc.get(expectedInstance).andExpect {
      status { isBadRequest() }
      hasStandardApiResponseFields(expectedInstance, status)
      hasErrorFields(status)
    }
  }
}
