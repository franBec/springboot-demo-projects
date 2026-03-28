package dev.pollito.spring_groovy.sakila.film.domain.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import java.time.OffsetDateTime

@Canonical
@CompileStatic
class Film {
  Integer id
  String title
  String description
  Integer releaseYear
  FilmRating rating
  Integer length
  FilmLanguage language
  FilmLanguage originalLanguage
  Integer rentalDuration
  BigDecimal rentalRate
  BigDecimal replacementCost
  String specialFeatures
  OffsetDateTime lastUpdate
}
