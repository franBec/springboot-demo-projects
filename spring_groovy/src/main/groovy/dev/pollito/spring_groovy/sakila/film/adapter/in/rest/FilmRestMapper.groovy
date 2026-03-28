package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.generated.model.Film as GeneratedFilm
import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
@CompileStatic
class FilmRestMapper {
  private final ModelMapper mapper

  FilmRestMapper(ModelMapper mapper) {
    this.mapper = mapper
  }

  GeneratedFilm map(Film source) {
    mapper.map(source, GeneratedFilm)
  }
}
