package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.adapter.in.rest.dto.FilmResponse
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
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

  FilmResponse convert(Film source) {
    mapper.map(source, FilmResponse)
  }
}
