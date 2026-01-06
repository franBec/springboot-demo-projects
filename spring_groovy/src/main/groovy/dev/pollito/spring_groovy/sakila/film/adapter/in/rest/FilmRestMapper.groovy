package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.generated.model.Film as RestDtoFilm
import dev.pollito.spring_groovy.sakila.film.domain.model.Film as DomainFilm
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

  RestDtoFilm convert(DomainFilm source) {
    mapper.map(source, RestDtoFilm)
  }
}
