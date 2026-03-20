package dev.pollito.spring_kotlin

import java.util.regex.Pattern
import java.util.regex.Pattern.compile
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension::class)
@ActiveProfiles("test")
@Sql(
    scripts = ["/sakila-schema.sql", "/sakila-data.sql"],
    executionPhase = BEFORE_TEST_CLASS,
)
class SanityCheckSpringBootTest {

  @Autowired private lateinit var mockMvc: MockMvc

  data class TestCase(
      val method: HttpMethod,
      val url: String,
      val pathParams: List<Any> = emptyList(),
      val headers: Map<String, String> = emptyMap(),
      val queryParams: Map<String, String> = emptyMap(),
      val requestBody: String? = null,
  )

  companion object {
    @JvmStatic
    fun sanityCheckTestCases(): List<TestCase> =
        listOf(
            TestCase(
                method = HttpMethod.GET,
                url = "/api/films/{id}",
                pathParams = listOf(1),
                headers =
                    mapOf(
                        "Authorization" to "Bearer secret-token",
                        "X-Api-Key" to "my-secret-key",
                    ),
            ),
            TestCase(
                method = HttpMethod.GET,
                url = "/api/films",
            ),
        )
  }

  private fun resolvePathParameters(url: String, pathParams: List<Any>): String {
    var resolved = url
    for (param in pathParams) {
      resolved = resolved.replaceFirst("\\{[^}]+}".toRegex(), param.toString())
    }
    return resolved
  }

  private fun countMatches(text: String, regex: String): Long =
      compile(regex).matcher(text).results().count()

  private fun assertLogFilterOutput(logOutput: String, method: HttpMethod, url: String) {
    val methodAndUri = ">>>> Method: ${method.name()}; URI: $url"
    assert(
        countMatches(
            logOutput,
            Pattern.quote(methodAndUri) + "; QueryString: [^;\\n]*; Headers: \\{[^\\n]*}",
        ) == 1L
    ) {
      "LogFilter should log request details with method, URI, QueryString, and Headers exactly once"
    }
    assert(countMatches(logOutput, "<<<< Response Status: \\d+") == 1L) {
      "LogFilter should log response status exactly once"
    }
  }

  private fun assertLogAspectOutput(logOutput: String) {
    assert(countMatches(logOutput, "\\[[\\w.]+\\([..]*\\)] Args: \\[") == 1L) {
      "LogAspect should log args with format [ClassName.methodName(..)] Args: [...] exactly once"
    }
    assert(countMatches(logOutput, "\\[[\\w.]+\\([..]*\\)] Response: <") == 1L) {
      "LogAspect should log response with format [ClassName.methodName(..)] Response: <...> exactly once"
    }
  }

  private fun assertMaskingPatternLayoutOutput(logOutput: String) {
    if (logOutput.contains("Authorization:") || logOutput.contains("X-Api-Key:")) {
      assert(!logOutput.contains("secret-token")) {
        "MaskingPatternLayout should mask Authorization value"
      }
      assert(!logOutput.contains("my-secret-key")) {
        "MaskingPatternLayout should mask X-Api-Key value"
      }
      assert(logOutput.contains("Authorization: ****")) {
        "MaskingPatternLayout should show masked Authorization"
      }
      assert(logOutput.contains("X-Api-Key: ****")) {
        "MaskingPatternLayout should show masked X-Api-Key"
      }
    }
  }

  private fun assertTraceIdFilterOutput(logOutput: String) {
    assert(logOutput.matches(Regex("(?s).*(trace_id=|trace_id=[a-f0-9]{32}).*"))) {
      "TraceIdFilter should add trace_id to MDC (if present, must be exactly 32 hex characters)"
    }
    assert(logOutput.matches(Regex("(?s).*(span_id=|span_id=[a-f0-9]{16}).*"))) {
      "TraceIdFilter should add span_id to MDC (if present, must be exactly 16 hex characters)"
    }
    assert(logOutput.matches(Regex("(?s).*trace_flags=(|00|01).*"))) {
      "TraceIdFilter should add trace_flags to MDC (empty, 00, or 01)"
    }
  }

  @ParameterizedTest
  @MethodSource("sanityCheckTestCases")
  fun sanityCheck(testCase: TestCase, output: CapturedOutput) {
    val resolvedUrl = resolvePathParameters(testCase.url, testCase.pathParams)
    val params = testCase.pathParams.toTypedArray()

    mockMvc.perform(
        testCase.method,
        testCase.url,
        params,
        testCase.headers,
        testCase.queryParams,
        testCase.requestBody,
    )

    val logOutput = output.out

    assertLogFilterOutput(logOutput, testCase.method, resolvedUrl)
    assertLogAspectOutput(logOutput)
    assertMaskingPatternLayoutOutput(logOutput)
    assertTraceIdFilterOutput(logOutput)
  }

  private fun MockMvc.perform(
      method: HttpMethod,
      url: String,
      pathParams: Array<Any>,
      headers: Map<String, String>,
      queryParams: Map<String, String>,
      requestBody: String?,
  ) {
    when (method.name()) {
      "GET" ->
          get(url, *pathParams) {
            accept = APPLICATION_JSON
            headers.forEach { (k, v) -> header(k, v) }
            queryParams.forEach { (k, v) -> param(k, v) }
          }
      "POST" ->
          post(url, *pathParams) {
            accept = APPLICATION_JSON
            headers.forEach { (k, v) -> header(k, v) }
            queryParams.forEach { (k, v) -> param(k, v) }
            requestBody?.let {
              content = it
              contentType = APPLICATION_JSON
            }
          }
      "PUT" ->
          put(url, *pathParams) {
            accept = APPLICATION_JSON
            headers.forEach { (k, v) -> header(k, v) }
            queryParams.forEach { (k, v) -> param(k, v) }
            requestBody?.let {
              content = it
              contentType = APPLICATION_JSON
            }
          }
      "PATCH" ->
          patch(url, *pathParams) {
            accept = APPLICATION_JSON
            headers.forEach { (k, v) -> header(k, v) }
            queryParams.forEach { (k, v) -> param(k, v) }
            requestBody?.let {
              content = it
              contentType = APPLICATION_JSON
            }
          }
      "DELETE" ->
          delete(url, *pathParams) {
            accept = APPLICATION_JSON
            headers.forEach { (k, v) -> header(k, v) }
            queryParams.forEach { (k, v) -> param(k, v) }
          }
      else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
    }
  }
}
