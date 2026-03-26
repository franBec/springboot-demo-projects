package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static io.opentelemetry.api.trace.Span.current;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import dev.pollito.spring_java.sakila.generated.api.FilmsApi;
import dev.pollito.spring_java.sakila.generated.model.FilmFields;
import dev.pollito.spring_java.sakila.generated.model.FilmListResponse;
import dev.pollito.spring_java.sakila.generated.model.FilmResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FilmRestController implements FilmsApi {
  private final FilmUseCases useCases;
  private final FilmRestMapper mapper;
  private final HttpServletRequest request;

  @Override
  public ResponseEntity<FilmResponse> createFilm(FilmFields filmFields) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public ResponseEntity<Void> deleteFilm(Integer id) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public ResponseEntity<FilmResponse> getFilm(Integer id) {
    return ok(
        new FilmResponse()
            .data(mapper.map(useCases.getFilm(id)))
            .instance(request.getRequestURI())
            .timestamp(now())
            .trace(current().getSpanContext().getTraceId())
            .status(OK.value()));
  }

  @Override
  public ResponseEntity<FilmListResponse> getFilms(Integer page, Integer size, List<String> sort) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public ResponseEntity<FilmResponse> updateFilm(Integer id, FilmFields filmFields) {
    throw new RuntimeException("Not implemented");
  }
}
