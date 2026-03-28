package dev.pollito.spring_kotlin.sakila.film.domain.service

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmRating
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import java.math.BigDecimal
import java.time.OffsetDateTime.parse
import org.springframework.stereotype.Service

@Service
class FilmUseCasesImpl : FilmUseCases {
  override fun getFilm(id: Int): Film {
    return Film(
        id = id,
        title = "ACADEMY DINOSAUR",
        description =
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
        releaseYear = 2006,
        rating = FilmRating.PG,
        length = 86,
        language = FilmLanguage.ENGLISH,
        originalLanguage = FilmLanguage.ENGLISH,
        rentalDuration = 3,
        rentalRate = BigDecimal("4.99"),
        replacementCost = BigDecimal("20.99"),
        specialFeatures = "Trailers,Deleted Scenes",
        lastUpdate = parse("2006-02-15T04:03:42Z"),
    )
  }
}
