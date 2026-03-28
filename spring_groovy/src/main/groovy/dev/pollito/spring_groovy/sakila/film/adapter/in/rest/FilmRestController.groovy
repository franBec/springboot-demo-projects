package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.ResponseEntity.ok

import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
import dev.pollito.spring_groovy.sakila.generated.api.FilmsApi
import dev.pollito.spring_groovy.sakila.generated.model.FilmFields
import dev.pollito.spring_groovy.sakila.generated.model.FilmListResponse
import dev.pollito.spring_groovy.sakila.generated.model.FilmResponse
import groovy.transform.CompileStatic
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@CompileStatic
class FilmRestController implements FilmsApi {
  private final FilmUseCases useCases
  private final FilmRestMapper mapper
  private final HttpServletRequest request

  FilmRestController(FilmUseCases useCases, FilmRestMapper mapper, HttpServletRequest request) {
    this.useCases = useCases
    this.mapper = mapper
    this.request = request
  }

  @Override
  ResponseEntity<FilmResponse> createFilm(FilmFields filmFields) {
    throw new RuntimeException("Not implemented")
  }

  @Override
  ResponseEntity<Void> deleteFilm(Integer id) {
    throw new RuntimeException("Not implemented")
  }

  @Override
  ResponseEntity<FilmResponse> getFilm(Integer id) {
    ok(
        new FilmResponse()
        .data(mapper.map(useCases.getFilm(id)))
        .instance(request.requestURI)
        .timestamp(now())
        .trace(Span.current().spanContext.traceId)
        .status(OK.value())
        )
  }

  @Override
  ResponseEntity<FilmListResponse> getFilms(Integer page, Integer size, List<String> sort) {
    throw new RuntimeException("Not implemented")
  }

  @Override
  ResponseEntity<FilmResponse> updateFilm(Integer id, FilmFields filmFields) {
    throw new RuntimeException("Not implemented")
  }
}
