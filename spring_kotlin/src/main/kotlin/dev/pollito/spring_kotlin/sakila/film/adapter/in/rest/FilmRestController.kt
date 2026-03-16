package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.generated.api.FilmsApi
import dev.pollito.spring_kotlin.generated.model.FilmListResponse
import dev.pollito.spring_kotlin.generated.model.FilmResponse
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FindAllPortIn
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FindByIdPortIn
import io.opentelemetry.api.trace.Span.current
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime.now
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

@RestController
class FilmRestController(
    private val findByIdPortIn: FindByIdPortIn,
    private val findAllPortIn: FindAllPortIn,
    private val mapper: FilmRestMapper,
    private val request: HttpServletRequest,
) : FilmsApi {
  override fun findAll(pageable: Pageable): ResponseEntity<FilmListResponse> {
    return ok(
        FilmListResponse(
            data = mapper.convert(findAllPortIn.findAll(pageable)),
            instance = request.requestURI,
            timestamp = now(),
            trace = current().spanContext.traceId,
            status = OK.value(),
        )
    )
  }

  override fun findById(id: Int): ResponseEntity<FilmResponse> {
    return ok(
        FilmResponse(
            data = mapper.convert(findByIdPortIn.findById(id)),
            instance = request.requestURI,
            timestamp = now(),
            trace = current().spanContext.traceId,
            status = OK.value(),
        )
    )
  }
}
