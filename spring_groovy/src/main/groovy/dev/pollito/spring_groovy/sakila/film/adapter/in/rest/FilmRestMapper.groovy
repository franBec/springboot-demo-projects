package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.config.enums.EnumUtils
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.generated.model.Film as GeneratedFilm
import dev.pollito.spring_groovy.sakila.generated.model.FilmFields
import dev.pollito.spring_groovy.sakila.generated.model.FilmLanguage as GeneratedFilmLanguage
import dev.pollito.spring_groovy.sakila.generated.model.FilmListResponseAllOfData
import dev.pollito.spring_groovy.sakila.generated.model.FilmRating as GeneratedFilmRating
import dev.pollito.spring_groovy.sakila.generated.model.Pageable as GeneratedPageable
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class FilmRestMapper {
  private final ModelMapper mapper
  private static final List<String> TRIVIAL_FIELDS = [
    'title',
    'description',
    'releaseYear',
    'length',
    'rentalDuration',
    'specialFeatures'
  ]

  FilmRestMapper(ModelMapper mapper) {
    this.mapper = mapper
    configureTypeMaps()
  }

  private void configureTypeMaps() {
    mapper.createTypeMap(FilmFields, Film).setConverter { ctx ->
      def s = ctx.source
      def d = new Film()
      TRIVIAL_FIELDS.each { d[it] = s[it] }
      d.language = s.language ? EnumUtils.fromValue(FilmLanguage, s.language.value) : null
      d.originalLanguage = s.originalLanguage ? EnumUtils.fromValue(FilmLanguage, s.originalLanguage.value) : null
      d.rentalRate = s.rentalRate != null ? BigDecimal.valueOf(s.rentalRate) : null
      d.replacementCost = s.replacementCost != null ? BigDecimal.valueOf(s.replacementCost) : null
      d.rating = s.rating ? EnumUtils.fromValue(FilmRating, s.rating.value) : null
      d
    }

    mapper.createTypeMap(Film, GeneratedFilm).setConverter { ctx ->
      def s = ctx.source
      def d = new GeneratedFilm()
      TRIVIAL_FIELDS.each { d[it] = s[it] }
      d.id = s.id
      d.lastUpdate = s.lastUpdate
      d.language = s.language ? GeneratedFilmLanguage.fromValue(s.language.value) : null
      d.originalLanguage = s.originalLanguage ? GeneratedFilmLanguage.fromValue(s.originalLanguage.value) : null
      d.rentalRate = s.rentalRate?.doubleValue()
      d.replacementCost = s.replacementCost?.doubleValue()
      d.rating = s.rating ? GeneratedFilmRating.fromValue(s.rating.value) : null
      d
    }
  }

  Film map(FilmFields source) {
    mapper.map(source, Film)
  }

  GeneratedFilm map(Film source) {
    mapper.map(source, GeneratedFilm)
  }

  FilmListResponseAllOfData map(Page<Film> source) {
    new FilmListResponseAllOfData(
        source.hasContent() ? source.content.collect { map(it) } : [],
        new GeneratedPageable(source.pageable.pageNumber, source.pageable.pageSize),
        (int) source.totalElements,
        source.totalPages
        )
  }
}
