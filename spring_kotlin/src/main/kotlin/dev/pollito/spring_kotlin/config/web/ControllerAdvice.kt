package dev.pollito.spring_kotlin.config.web

import dev.pollito.spring_kotlin.sakila.generated.model.Error
import io.github.oshai.kotlinlogging.KotlinLogging
import io.opentelemetry.api.trace.Span.current
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime.now
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class ControllerAdvice(private val request: HttpServletRequest) {

  private fun buildErrorResponse(e: Exception, status: HttpStatus): ResponseEntity<Error> {
    val exceptionSimpleName = e.javaClass.simpleName
    val logMessage = "$exceptionSimpleName being handled"

    when {
      status.is5xxServerError -> log.error(e) { logMessage }
      status.is4xxClientError -> log.warn(e) { logMessage }
      else -> log.info(e) { logMessage }
    }

    return ResponseEntity.status(status)
        .body(
            Error(
                detail = e.localizedMessage,
                instance = request.requestURI,
                status = status.value(),
                timestamp = now(),
                title = status.reasonPhrase,
                trace = current().spanContext.traceId,
            )
        )
  }

  @ExceptionHandler(Exception::class)
  fun handle(e: Exception): ResponseEntity<Error> {
    return buildErrorResponse(e, INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(NoResourceFoundException::class)
  fun handle(e: NoResourceFoundException): ResponseEntity<Error> {
    return buildErrorResponse(e, NOT_FOUND)
  }
}
