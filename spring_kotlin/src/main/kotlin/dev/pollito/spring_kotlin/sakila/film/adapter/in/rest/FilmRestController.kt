package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import dev.pollito.spring_kotlin.sakila.generated.api.FilmsApi
import dev.pollito.spring_kotlin.sakila.generated.model.FilmFields
import dev.pollito.spring_kotlin.sakila.generated.model.FilmListResponse
import dev.pollito.spring_kotlin.sakila.generated.model.FilmResponse
import io.opentelemetry.api.trace.Span.current
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime.now
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.RestController

@RestController
class FilmRestController(
    private val useCases: FilmUseCases,
    private val mapper: FilmRestMapper,
    private val request: HttpServletRequest,
) : FilmsApi {

  override fun createFilm(filmFields: FilmFields): ResponseEntity<FilmResponse> {
    return status(CREATED)
        .body(
            FilmResponse(
                data = mapper.map(useCases.createFilm(mapper.map(filmFields))),
                instance = request.requestURI,
                timestamp = now(),
                trace = current().spanContext.traceId,
                status = CREATED.value(),
            )
        )
  }

  override fun deleteFilm(id: Int): ResponseEntity<Unit> {
    useCases.deleteFilm(id)
    return ResponseEntity.noContent().build()
  }

  override fun getFilm(id: Int): ResponseEntity<FilmResponse> {
    return ok(
        FilmResponse(
            data = mapper.map(useCases.getFilm(id)),
            instance = request.requestURI,
            timestamp = now(),
            trace = current().spanContext.traceId,
            status = OK.value(),
        )
    )
  }

  override fun getFilms(pageable: Pageable): ResponseEntity<FilmListResponse> {
    return ok(
        FilmListResponse(
            data = mapper.map(useCases.getFilms(pageable)),
            instance = request.requestURI,
            timestamp = now(),
            trace = current().spanContext.traceId,
            status = OK.value(),
        )
    )
  }

  override fun updateFilm(id: Int, filmFields: FilmFields): ResponseEntity<FilmResponse> {
    return ok(
        FilmResponse(
            data = mapper.map(useCases.updateFilm(id, mapper.map(filmFields))),
            instance = request.requestURI,
            timestamp = now(),
            trace = current().spanContext.traceId,
            status = OK.value(),
        )
    )
  }
}
