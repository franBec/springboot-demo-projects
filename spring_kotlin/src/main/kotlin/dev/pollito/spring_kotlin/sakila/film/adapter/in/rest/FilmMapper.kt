package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto.FilmResponse
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.stereotype.Component

@Component
class FilmMapper {
  fun convert(source: Film?): FilmResponse? {
    if (source == null) {
      return null
    }
    return FilmResponse(
        id = source.id,
        title = source.title,
        description = source.description,
        releaseYear = source.releaseYear,
        rating = source.rating,
        lengthMinutes = source.lengthMinutes,
        language = source.language,
    )
  }
}
