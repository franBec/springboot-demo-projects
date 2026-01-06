package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static io.opentelemetry.api.trace.Span.current;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import dev.pollito.spring_java.generated.api.FilmsApi;
import dev.pollito.spring_java.generated.model.FilmListResponse;
import dev.pollito.spring_java.generated.model.FilmResponse;
import dev.pollito.spring_java.sakila.film.domain.port.in.FindByIdPortIn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FilmRestController implements FilmsApi {
  private final FindByIdPortIn findByIdPortIn;
  private final FilmRestMapper mapper;
  private final HttpServletRequest request;

  @Override
  public ResponseEntity<FilmListResponse> findAll() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseEntity<FilmResponse> findById(Integer id) {
    return ok(
        new FilmResponse()
            .data(mapper.convert(findByIdPortIn.findById(id)))
            .instance(request.getRequestURI())
            .timestamp(now())
            .trace(current().getSpanContext().getTraceId())
            .status(OK.value()));
  }
}
