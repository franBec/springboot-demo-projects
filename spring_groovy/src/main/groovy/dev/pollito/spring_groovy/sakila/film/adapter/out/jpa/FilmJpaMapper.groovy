package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.config.enums.EnumUtils
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.generated.entity.Film as GeneratedFilm
import dev.pollito.spring_groovy.sakila.generated.entity.Language
import java.time.LocalDate
import java.time.ZoneOffset
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class FilmJpaMapper {
  private final ModelMapper mapper
  private static final List<String> TRIVIAL_FIELDS = [
    'title',
    'description',
    'length',
    'rentalDuration',
    'rentalRate',
    'replacementCost',
    'specialFeatures'
  ]

  FilmJpaMapper(ModelMapper mapper) {
    this.mapper = mapper
    configureTypeMaps()
  }

  private void configureTypeMaps() {
    mapper.createTypeMap(GeneratedFilm, Film).setConverter { ctx ->
      def s = ctx.source
      def d = new Film()
      TRIVIAL_FIELDS.each { d[it] = s[it] }
      d.id = s.filmId
      d.releaseYear = s.releaseYear?.year
      d.rating = s.rating ? EnumUtils.fromValue(FilmRating, s.rating) : null
      d.language = EnumUtils.fromValue(FilmLanguage, s.languageByLanguageId.name)
      d.originalLanguage = s.languageByOriginalLanguageId?.name
          ? EnumUtils.fromValue(FilmLanguage, s.languageByOriginalLanguageId.name)
          : null
      d.lastUpdate = s.lastUpdate?.atOffset(ZoneOffset.UTC)
      d
    }

    mapper.createTypeMap(Film, GeneratedFilm).setConverter { ctx ->
      def s = ctx.source
      def d = new GeneratedFilm()
      TRIVIAL_FIELDS.each { d[it] = s[it] }
      d.releaseYear = s.releaseYear ? LocalDate.of(s.releaseYear, 1, 1) : null
      d.rating = s.rating?.value
      d
    }
  }

  GeneratedFilm map(Film source, Language language, Language originalLanguage) {
    def entity = mapper.map(source, GeneratedFilm)
    entity.languageByLanguageId = language
    entity.languageByOriginalLanguageId = originalLanguage
    entity
  }

  Film map(GeneratedFilm source) {
    mapper.map(source, Film)
  }

  Page<Film> map(Page<GeneratedFilm> source) {
    source.map { map(it) }
  }
}
