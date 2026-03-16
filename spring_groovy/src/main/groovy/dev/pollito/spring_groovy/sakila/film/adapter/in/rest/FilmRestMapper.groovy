package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.generated.model.Film as RestDtoFilm
import dev.pollito.spring_groovy.generated.model.FilmListResponseAllOfData
import dev.pollito.spring_groovy.generated.model.Pageable as PageableDto
import dev.pollito.spring_groovy.sakila.film.domain.model.Film as DomainFilm
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

  RestDtoFilm convert(DomainFilm source) {
    mapper.map(source, RestDtoFilm)
  }

  FilmListResponseAllOfData convert(Page<DomainFilm> source) {
    new FilmListResponseAllOfData(
        content: source.content.collect { convert(it) },
        pageable: new PageableDto(
        pageNumber: source.pageable.pageNumber,
        pageSize: source.pageable.pageSize,
        ),
        totalElements: (int) source.totalElements,
        totalPages: source.totalPages,
        )
  }
}
