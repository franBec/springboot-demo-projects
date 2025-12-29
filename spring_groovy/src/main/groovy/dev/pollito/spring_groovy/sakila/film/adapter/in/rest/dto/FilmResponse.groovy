package dev.pollito.spring_groovy.sakila.film.adapter.in.rest.dto

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class FilmResponse {
  Long id
  String title
  String description
  Integer releaseYear
  String rating
  Integer lengthMinutes
  String language
}
