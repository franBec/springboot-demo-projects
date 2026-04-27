package dev.pollito.spring_java.sakila.auth.adapter.in.rest;

import static io.opentelemetry.api.trace.Span.current;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import dev.pollito.spring_java.sakila.auth.domain.port.in.AuthUseCases;
import dev.pollito.spring_java.sakila.generated.api.AuthApi;
import dev.pollito.spring_java.sakila.generated.model.LoginRequest;
import dev.pollito.spring_java.sakila.generated.model.LoginResponse;
import dev.pollito.spring_java.sakila.generated.model.LoginResponseAllOfData;
import dev.pollito.spring_java.sakila.generated.model.UserDetailsResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthRestController implements AuthApi {

  private final AuthUseCases authUseCases;
  private final AuthRestMapper mapper;
  private final HttpServletRequest request;

  @Override
  public ResponseEntity<LoginResponse> login(@NonNull LoginRequest loginRequest) {
    return ok(
        new LoginResponse()
            .data(
                new LoginResponseAllOfData()
                    .token(
                        authUseCases.authenticate(
                            loginRequest.getUsername(), loginRequest.getPassword())))
            .instance(request.getRequestURI())
            .status(OK.value())
            .timestamp(now())
            .trace(current().getSpanContext().getTraceId()));
  }

  @Override
  public ResponseEntity<UserDetailsResponse> getCurrentUserDetails() {
    return ok(
        new UserDetailsResponse()
            .data(mapper.map(authUseCases.getCurrentUser()))
            .instance(request.getRequestURI())
            .timestamp(now())
            .trace(current().getSpanContext().getTraceId())
            .status(OK.value()));
  }
}
