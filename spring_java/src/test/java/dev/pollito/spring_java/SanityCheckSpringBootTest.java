package dev.pollito.spring_java;

import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class SanityCheckSpringBootTest {

  @SuppressWarnings("unused")
  @Autowired
  private MockMvc mockMvc;

  record TestCase(
      @NonNull HttpMethod method,
      @NonNull String url,
      @NonNull List<Object> pathParams,
      @NonNull Map<String, String> headers,
      @NonNull Map<String, String> queryParams,
      @Nullable String requestBody) {}

  static @NonNull Stream<TestCase> sanityCheckTestCases() {
    return Stream.of(
        new TestCase(
            HttpMethod.GET,
            "/api/films/{id}",
            List.of(1),
            /* we don't have yet any endpoint with sensible headers to mask, so let's use this one for now */
            Map.of("Authorization", "Bearer secret-token", "X-Api-Key", "my-secret-key"),
            Collections.emptyMap(),
            null));
  }

  private MockHttpServletRequestBuilder buildRequest(
      @NonNull HttpMethod method, @NonNull String url, @NonNull List<Object> pathParams) {
    Object[] params = pathParams.toArray();
    return switch (method.name()) {
      case "GET" -> get(url, params);
      case "POST" -> post(url, params);
      case "PUT" -> put(url, params);
      case "PATCH" -> patch(url, params);
      case "DELETE" -> delete(url, params);
      default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
    };
  }

  private String resolvePathParameters(@NonNull String url, @NonNull List<Object> pathParams) {
    String resolved = url;
    for (Object param : pathParams) {
      resolved = resolved.replaceFirst("\\{[^}]+}", String.valueOf(param));
    }
    return resolved;
  }

  private long countMatches(@NonNull String text, @NonNull String regex) {
    return compile(regex).matcher(text).results().count();
  }

  private void assertLogFilterOutput(
      @NonNull String logOutput, @NonNull HttpMethod method, @NonNull String url) {
    String methodAndUri = String.format(">>>> Method: %s; URI: %s", method.name(), url);
    assert countMatches(
                logOutput, quote(methodAndUri) + "; QueryString: [^;\\n]*; Headers: \\{[^\\n]*}")
            == 1
        : "LogFilter should log request details with method, URI, QueryString, and Headers exactly once";
    assert countMatches(logOutput, "<<<< Response Status: \\d+") == 1
        : "LogFilter should log response status exactly once";
  }

  private void assertLogAspectOutput(@NonNull String logOutput) {
    assert countMatches(logOutput, "\\[[\\w.]+\\([..]*\\)] Args: \\[") == 1
        : "LogAspect should log args with format [ClassName.methodName(..)] Args: [...] exactly once";
    assert countMatches(logOutput, "\\[[\\w.]+\\([..]*\\)] Response: <") == 1
        : "LogAspect should log response with format [ClassName.methodName(..)] Response: <...> exactly once";
  }

  private void assertMaskingPatternLayoutOutput(@NonNull String logOutput) {
    if (logOutput.contains("Authorization:") || logOutput.contains("X-Api-Key:")) {
      assert !logOutput.contains("secret-token")
          : "MaskingPatternLayout should mask Authorization value";
      assert !logOutput.contains("my-secret-key")
          : "MaskingPatternLayout should mask X-Api-Key value";
      assert logOutput.contains("Authorization: ****")
          : "MaskingPatternLayout should show masked Authorization";
      assert logOutput.contains("X-Api-Key: ****")
          : "MaskingPatternLayout should show masked X-Api-Key";
    }
  }

  private void assertTraceIdFilterOutput(@NonNull String logOutput) {
    assert logOutput.matches("(?s).*(trace_id=|trace_id=[a-f0-9]{32}).*")
        : "TraceIdFilter should add trace_id to MDC (if present, must be exactly 32 hex characters)";
    assert logOutput.matches("(?s).*(span_id=|span_id=[a-f0-9]{16}).*")
        : "TraceIdFilter should add span_id to MDC (if present, must be exactly 16 hex characters)";
    assert logOutput.matches("(?s).*trace_flags=(|00|01).*")
        : "TraceIdFilter should add trace_flags to MDC (empty, 00, or 01)";
  }

  @ParameterizedTest
  @MethodSource("sanityCheckTestCases")
  void sanityCheck(@NonNull TestCase testCase, @NonNull CapturedOutput output) throws Exception {
    MockHttpServletRequestBuilder requestBuilder =
        buildRequest(testCase.method(), testCase.url(), testCase.pathParams());

    testCase.headers().forEach(requestBuilder::header);
    testCase.queryParams().forEach(requestBuilder::param);
    if (testCase.requestBody() != null) {
      requestBuilder.content(testCase.requestBody()).contentType(APPLICATION_JSON);
    }

    mockMvc.perform(requestBuilder.accept(APPLICATION_JSON));
    String logOutput = output.getOut();

    assertLogFilterOutput(
        logOutput, testCase.method(), resolvePathParameters(testCase.url(), testCase.pathParams()));
    assertLogAspectOutput(logOutput);
    assertMaskingPatternLayoutOutput(logOutput);
    assertTraceIdFilterOutput(logOutput);
  }
}
