package dev.pollito.spring_groovy.sakila.film.domain.service

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
import groovy.transform.CompileStatic
import java.time.OffsetDateTime
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FilmUseCasesImpl implements FilmUseCases {
  @Override
  Film getFilm(Integer id) {
    new Film(
        id: id,
        title: "ACADEMY DINOSAUR",
        description: "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
        releaseYear: 2006,
        rating: FilmRating.PG,
        length: 86,
        language: FilmLanguage.ENGLISH,
        originalLanguage: FilmLanguage.ENGLISH,
        rentalDuration: 3,
        rentalRate: 4.99G,
        replacementCost: 20.99G,
        specialFeatures: "Trailers,Deleted Scenes",
        lastUpdate: OffsetDateTime.parse("2006-02-15T04:03:42Z")
        )
  }
}
