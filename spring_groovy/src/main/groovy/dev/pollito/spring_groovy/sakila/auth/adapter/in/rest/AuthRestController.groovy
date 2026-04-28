package dev.pollito.spring_groovy.sakila.auth.adapter.in.rest

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.ResponseEntity.ok

import dev.pollito.spring_groovy.sakila.auth.domain.port.in.AuthUseCases
import dev.pollito.spring_groovy.sakila.generated.api.AuthApi
import dev.pollito.spring_groovy.sakila.generated.model.LoginRequest
import dev.pollito.spring_groovy.sakila.generated.model.LoginResponse
import dev.pollito.spring_groovy.sakila.generated.model.LoginResponseAllOfData
import dev.pollito.spring_groovy.sakila.generated.model.UserDetailsResponse
import groovy.transform.CompileStatic
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@CompileStatic
class AuthRestController implements AuthApi {
  private final AuthUseCases authUseCases
  private final AuthRestMapper mapper
  private final HttpServletRequest request

  AuthRestController(AuthUseCases authUseCases, AuthRestMapper mapper, HttpServletRequest request) {
    this.authUseCases = authUseCases
    this.mapper = mapper
    this.request = request
  }

  @Override
  ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
    ok(
        new LoginResponse()
        .data(
        new LoginResponseAllOfData()
        .token(authUseCases.authenticate(loginRequest.username, loginRequest.password))
        )
        .instance(request.requestURI)
        .status(OK.value())
        .timestamp(now())
        .trace(Span.current().spanContext.traceId)
        )
  }

  @Override
  ResponseEntity<UserDetailsResponse> getCurrentUserDetails() {
    ok(
        new UserDetailsResponse()
        .data(mapper.map(authUseCases.getCurrentUser()))
        .instance(request.requestURI)
        .status(OK.value())
        .timestamp(now())
        .trace(Span.current().spanContext.traceId)
        )
  }
}
