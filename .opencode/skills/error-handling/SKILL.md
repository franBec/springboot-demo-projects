---
name: error-handling
description: Implement standardized error handling in Spring Boot modules following RFC 9457 Problem Details
license: MIT
compatibility: opencode
metadata:
  language: java,kotlin,groovy
  framework: spring-boot
---

## What I do

- Guide implementation of `@RestControllerAdvice` error handlers
- Ensure RFC 9457 Problem Details compliance
- Standardize logging by HTTP status series
- Provide consistent error response format with OpenTelemetry tracing

## When to use me

Use this skill when:

- Creating a new `ControllerAdvice` class
- Adding exception handlers for new exception types
- Writing tests for error handling
- Reviewing error response formats

## ControllerAdvice Location

- Java: `spring_java/.../config/advice/ControllerAdvice.java`
- Kotlin: `spring_kotlin/.../config/advice/ControllerAdvice.kt`
- Groovy: `spring_groovy/.../config/advice/ControllerAdvice.groovy`

## Two-Tier Strategy

1. **Specific Exception Handlers**: Handle known exceptions with appropriate HTTP status codes (404, 400, etc.)
2. **Fallback Handler**: `@ExceptionHandler(Exception.class)` returns 500 for unhandled exceptions

### Response Format

| Field       | Description                   |
|-------------|-------------------------------|
| `status`    | HTTP status code              |
| `title`     | HTTP status reason phrase     |
| `detail`    | Exception's localized message |
| `instance`  | Request URI that caused error |
| `timestamp` | ISO 8601 timestamp            |
| `trace`     | OpenTelemetry trace ID        |

## Adding New Exception Handler

```java
@ExceptionHandler(SomeSpecificException.class)
public ProblemDetail handle(SomeSpecificException e) {
  return buildProblemDetail(e, BAD_REQUEST);
}
```

## Logging Strategy

| Status Series    | Log Level | Rationale                   |
|------------------|-----------|-----------------------------|
| 5xx Server Error | `ERROR`   | Bugs or system failures     |
| 4xx Client Error | `WARN`    | Client mistakes, API misuse |
| Other            | `INFO`    | Informational only          |

**Log Output**:
- Exception class simple name
- Full stack trace
- OpenTelemetry trace ID (via MDC)

## Testing

Test files: `ControllerAdviceTest.java` / `ControllerAdviceTest.kt` / `ControllerAdviceSpec.groovy`

**Verify for each handler**:

- Correct HTTP status code
- All fields present: `status`, `title`, `detail`, `instance`, `timestamp`, `trace`

**Test Utilities**:

- Java: `ApiResponseMatchers` utility class
- Kotlin: Extension functions
- Groovy: Trait mixed into specifications

## Language-Specific Notes

| Aspect      | Java            | Kotlin        | Groovy           |
|-------------|-----------------|---------------|------------------|
| Logging     | Lombok `@Slf4j` | KotlinLogging | Groovy `@Slf4j`  |
| Annotations | Lombok          | Native Kotlin | `@CompileStatic` |