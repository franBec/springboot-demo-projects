package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.ResponseEntity.ok

import dev.pollito.spring_groovy.generated.api.FilmsApi
import dev.pollito.spring_groovy.generated.model.FilmListResponse
import dev.pollito.spring_groovy.generated.model.FilmResponse
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindByIdPortIn
import groovy.transform.CompileStatic
import io.opentelemetry.api.trace.Span
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
@CompileStatic
class FilmRestController implements FilmsApi {
  FindByIdPortIn findByIdPortIn
  FilmRestMapper mapper
  HttpServletRequest request

  FilmRestController(FindByIdPortIn findByIdPortIn, FilmRestMapper mapper, HttpServletRequest request) {
    this.findByIdPortIn = findByIdPortIn
    this.mapper = mapper
    this.request = request
  }

  @Override
  ResponseEntity<FilmListResponse> findAll() {
    throw new UnsupportedOperationException()
  }

  @Override
  ResponseEntity<FilmResponse> findById(Integer id) {
    ok(
        new FilmResponse(
        data: mapper.convert(findByIdPortIn.findById(id)),
        instance: request.requestURI,
        timestamp: now(),
        trace: Span.current().spanContext.traceId,
        status: OK.value()
        )
        )
  }
}
