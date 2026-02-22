package dev.pollito.spring_groovy

import static java.util.regex.Pattern.compile
import static java.util.regex.Pattern.quote
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
@AutoConfigureMockMvc
class SanityCheckSpringBootSpec extends Specification {

  @Autowired
  MockMvc mockMvc

  ByteArrayOutputStream outputCapture
  PrintStream originalOut
  PrintStream originalErr

  def setup() {
    outputCapture = new ByteArrayOutputStream()
    originalOut = System.out
    originalErr = System.err
    def printStream = new PrintStream(outputCapture)
    System.setOut(printStream)
    System.setErr(printStream)
  }

  def cleanup() {
    System.setOut(originalOut)
    System.setErr(originalErr)
  }

  private static MockHttpServletRequestBuilder buildRequest(HttpMethod method, String url, List<Object> pathParams) {
    Object[] params = pathParams.toArray()
    switch (method.name()) {
      case "GET": return get(url, params)
      case "POST": return post(url, params)
      case "PUT": return put(url, params)
      case "PATCH": return patch(url, params)
      case "DELETE": return delete(url, params)
      default: throw new IllegalArgumentException("Unsupported HTTP method: ${method}")
    }
  }

  private static String resolvePathParameters(String url, List<Object> pathParams) {
    String resolved = url
    for (param in pathParams) {
      resolved = resolved.replaceFirst('\\{[^}]+}', String.valueOf(param))
    }
    resolved
  }

  private static long countMatches(String text, String regex) {
    compile(regex).matcher(text).results().count()
  }

  private static void assertLogFilterOutput(String logOutput, HttpMethod method, String url) {
    String methodAndUri = ">>>> Method: ${method.name()}; URI: ${url}"
    assert countMatches(
    logOutput, quote(methodAndUri) + '; QueryString: [^;\\n]*; Headers: \\{[^\\n]*}') == 1:
    'LogFilter should log request details with method, URI, QueryString, and Headers exactly once'
    assert countMatches(logOutput, '<<<< Response Status: \\d+') == 1:
    'LogFilter should log response status exactly once'
  }

  private static void assertLogAspectOutput(String logOutput) {
    assert countMatches(logOutput, '\\[[\\w.]+\\([..]*\\)] Args: ') == 1:
    'LogAspect should log args with format [ClassName.methodName(..)] Args: ... exactly once'
    assert countMatches(logOutput, '\\[[\\w.]+\\([..]*\\)] Response: <') == 1:
    'LogAspect should log response with format [ClassName.methodName(..)] Response: <...> exactly once'
  }

  private static void assertMaskingPatternLayoutOutput(String logOutput) {
    if (logOutput.contains('Authorization:') || logOutput.contains('X-Api-Key:')) {
      assert !logOutput.contains('secret-token'):
      'MaskingPatternLayout should mask Authorization value'
      assert !logOutput.contains('my-secret-key'):
      'MaskingPatternLayout should mask X-Api-Key value'
      assert logOutput.contains('Authorization: ****'):
      'MaskingPatternLayout should show masked Authorization'
      assert logOutput.contains('X-Api-Key: ****'):
      'MaskingPatternLayout should show masked X-Api-Key'
    }
  }

  private static void assertTraceIdFilterOutput(String logOutput) {
    assert logOutput ==~ /(?s).*(trace_id=|trace_id=[a-f0-9]{32}).*/
    assert logOutput ==~ /(?s).*(span_id=|span_id=[a-f0-9]{16}).*/
    assert logOutput ==~ /(?s).*trace_flags=(|00|01).*/
  }

  @Unroll
  def "sanityCheck #method #url"() {
    given:
    def requestBuilder = buildRequest(method, url, pathParams)
    headers.each { k, v -> requestBuilder.header(k, v) }
    queryParams.each { k, v -> requestBuilder.param(k, v) }
    if (requestBody != null) {
      requestBuilder.content(requestBody).contentType(APPLICATION_JSON)
    }

    when:
    mockMvc.perform(requestBuilder.accept(APPLICATION_JSON))

    then:
    def logOutput = outputCapture.toString()
    assertLogFilterOutput(logOutput, method, resolvePathParameters(url, pathParams))
    assertLogAspectOutput(logOutput)
    assertMaskingPatternLayoutOutput(logOutput)
    assertTraceIdFilterOutput(logOutput)

    // Reset output capture for next iteration
    outputCapture.reset()

    where:
    method         | url               | pathParams | headers                                                                          | queryParams       | requestBody
    HttpMethod.GET | "/api/films/{id}" | [1]        | ["Authorization": "Bearer secret-token", "X-Api-Key": "my-secret-key"] as Map   | [:] as Map        | null
  }
}
