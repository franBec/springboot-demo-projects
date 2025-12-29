package dev.pollito.spring_groovy.sakila.film.domain.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Film {
  Integer id
  String title
  String description
  Integer releaseYear
  String rating
  Integer length
  String language
}
