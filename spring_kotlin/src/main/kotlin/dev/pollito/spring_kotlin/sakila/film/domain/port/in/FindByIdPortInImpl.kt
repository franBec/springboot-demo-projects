package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.stereotype.Service

@Service
class FindByIdPortInImpl : FindByIdPortIn {
  override fun findById(id: Int): Film {
    return Film(
        id = id,
        title = "ACADEMY DINOSAUR",
        description =
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
        releaseYear = 2006,
        rating = "PG",
        length = 86,
        language = "English",
    )
  }
}
