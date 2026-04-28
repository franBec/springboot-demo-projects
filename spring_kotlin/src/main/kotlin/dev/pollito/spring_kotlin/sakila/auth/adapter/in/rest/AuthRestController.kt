package dev.pollito.spring_kotlin.sakila.auth.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.auth.domain.port.`in`.AuthUseCases
import dev.pollito.spring_kotlin.sakila.generated.api.AuthApi
import dev.pollito.spring_kotlin.sakila.generated.model.LoginRequest
import dev.pollito.spring_kotlin.sakila.generated.model.LoginResponse
import dev.pollito.spring_kotlin.sakila.generated.model.LoginResponseAllOfData
import dev.pollito.spring_kotlin.sakila.generated.model.UserDetailsResponse
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthRestController(
    private val authUseCases: AuthUseCases,
    private val authRestMapper: AuthRestMapper,
    private val request: HttpServletRequest,
) : AuthApi {

  override fun login(loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
    return ResponseEntity.ok(
        LoginResponse(
            instance = request.requestURI,
            status = 200,
            timestamp = OffsetDateTime.now(),
            trace = Span.current().spanContext.traceId,
            data =
                LoginResponseAllOfData(
                    authUseCases.authenticate(loginRequest.username, loginRequest.password)
                ),
        )
    )
  }

  override fun getCurrentUserDetails(): ResponseEntity<UserDetailsResponse> {
    val userDetails = authUseCases.getCurrentUser()
    return ResponseEntity.ok(
        UserDetailsResponse(
            instance = request.requestURI,
            status = 200,
            timestamp = OffsetDateTime.now(),
            trace = Span.current().spanContext.traceId,
            data = authRestMapper.map(userDetails),
        )
    )
  }
}
