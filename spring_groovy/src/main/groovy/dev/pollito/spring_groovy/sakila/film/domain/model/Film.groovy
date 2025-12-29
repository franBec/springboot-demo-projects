package dev.pollito.spring_groovy.sakila.film.domain.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Film {
  Long id
  String title
  String description
  Integer releaseYear
  String rating
  Integer lengthMinutes
  String language
}
