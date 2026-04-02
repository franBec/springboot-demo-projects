package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.common.util.EnumUtils
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.generated.entity.Film as GeneratedFilm
import groovy.transform.CompileStatic
import java.time.ZoneOffset
import org.modelmapper.ModelMapper
import org.modelmapper.TypeMap
import org.modelmapper.spi.MappingContext
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
@CompileStatic
class FilmJpaMapper {
  private final ModelMapper mapper
  private TypeMap<GeneratedFilm, Film> typeMap

  FilmJpaMapper(ModelMapper mapper) {
    this.mapper = mapper
    configureTypeMap()
  }

  private void configureTypeMap() {
    typeMap = mapper.createTypeMap(GeneratedFilm, Film)
    typeMap.setConverter { MappingContext<GeneratedFilm, Film> ctx ->
      GeneratedFilm source = ctx.source
      new Film(
          id: source.filmId,
          title: source.title,
          description: source.description,
          releaseYear: source.releaseYear?.year,
          length: source.length,
          rentalDuration: source.rentalDuration,
          rentalRate: source.rentalRate,
          replacementCost: source.replacementCost,
          specialFeatures: source.specialFeatures,
          rating: source.rating != null ? EnumUtils.fromValue(FilmRating, source.rating) : null,
          language: source.languageByLanguageId?.name != null
          ? EnumUtils.fromValue(FilmLanguage, source.languageByLanguageId.name)
          : null,
          originalLanguage: source.languageByOriginalLanguageId?.name != null
          ? EnumUtils.fromValue(FilmLanguage, source.languageByOriginalLanguageId.name)
          : null,
          lastUpdate: source.lastUpdate?.atOffset(ZoneOffset.UTC)
          )
    }
  }

  Film map(GeneratedFilm source) {
    mapper.map(source, Film)
  }

  Page<Film> map(Page<GeneratedFilm> source) {
    source.map {  it -> map(it) }
  }
}
