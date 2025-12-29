package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.adapter.in.rest.dto.FilmResponse
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
final class FilmMapper {
  private FilmMapper() {}

  @CompileDynamic
  static FilmResponse convert(Film source) {
    source ? new FilmResponse(
        source.properties.findAll {
          it.key != 'class'
        }
        ) : null
  }
}
