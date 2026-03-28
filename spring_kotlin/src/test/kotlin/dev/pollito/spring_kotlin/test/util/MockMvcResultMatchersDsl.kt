package dev.pollito.spring_kotlin.test.util

import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

fun MockMvcResultMatchersDsl.hasStandardApiResponseFields(
    expectedInstance: String,
    expectedStatus: HttpStatus,
) {
  jsonPath("$.instance") { value(expectedInstance) }
  jsonPath("$.status") { value(expectedStatus.value()) }
  jsonPath("$.timestamp") { exists() }
  jsonPath("$.trace") { exists() }
}

fun MockMvcResultMatchersDsl.hasErrorFields(extectedStatus: HttpStatus) {
  jsonPath("$.title") { value(extectedStatus.reasonPhrase) }
}
