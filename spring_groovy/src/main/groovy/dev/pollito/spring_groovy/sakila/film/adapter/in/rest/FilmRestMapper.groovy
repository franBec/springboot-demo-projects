package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.generated.model.Film as GeneratedFilm
import dev.pollito.spring_groovy.sakila.generated.model.FilmListResponseAllOfData
import dev.pollito.spring_groovy.sakila.generated.model.Pageable as GeneratedPageable
import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
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

  FilmListResponseAllOfData map(Page<Film> source) {
    new FilmListResponseAllOfData(
        source.hasContent() ? source.content.collect {  it -> map(it) } : [],
        new GeneratedPageable(source.pageable.pageNumber, source.pageable.pageSize),
        (int) source.totalElements,
        source.totalPages
        )
  }
}
