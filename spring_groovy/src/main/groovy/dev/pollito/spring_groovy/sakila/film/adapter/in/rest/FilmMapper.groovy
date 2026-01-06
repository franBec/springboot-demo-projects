package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
@CompileStatic
class FilmMapper {
  private final ModelMapper modelMapper

  FilmMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper
  }

  dev.pollito.spring_groovy.generated.model.Film convert(Film source) {
    if (!source) return null

    def target = modelMapper.map(source, dev.pollito.spring_groovy.generated.model.Film)

    if (source.rating) {
      target.rating = dev.pollito.spring_groovy.generated.model.Film.RatingEnum.fromValue(source.rating)
    }

    target
  }
}
