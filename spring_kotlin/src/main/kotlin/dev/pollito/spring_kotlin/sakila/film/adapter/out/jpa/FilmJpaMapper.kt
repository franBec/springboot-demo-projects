package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.config.enums.EnumUtils
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmRating
import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.springframework.data.domain.Page

@Mapper(
    componentModel = SPRING,
    imports =
        [
            EnumUtils::class,
            FilmLanguage::class,
            FilmRating::class,
            OffsetDateTime::class,
            ZoneOffset::class,
        ],
)
interface FilmJpaMapper {

  @Mapping(target = "id", source = "filmId")
  @Mapping(
      target = "language",
      expression =
          "java(source.getLanguageByLanguageId() != null && source.getLanguageByLanguageId().getName() != null ? EnumUtils.INSTANCE.fromValue(FilmLanguage.class, source.getLanguageByLanguageId().getName()) : null)",
  )
  @Mapping(
      target = "originalLanguage",
      expression =
          "java(source.getLanguageByOriginalLanguageId() != null && source.getLanguageByOriginalLanguageId().getName() != null ? EnumUtils.INSTANCE.fromValue(FilmLanguage.class, source.getLanguageByOriginalLanguageId().getName()) : null)",
  )
  @Mapping(
      target = "releaseYear",
      expression =
          "java(source.getReleaseYear() != null ? source.getReleaseYear().getYear() : null)",
  )
  @Mapping(
      target = "rating",
      expression =
          "java(source.getRating() != null ? EnumUtils.INSTANCE.fromValue(FilmRating.class, source.getRating()) : null)",
  )
  @Mapping(
      target = "lastUpdate",
      expression =
          "java(source.getLastUpdate() != null ? source.getLastUpdate().atOffset(ZoneOffset.UTC) : null)",
  )
  fun map(source: dev.pollito.spring_kotlin.sakila.generated.entity.Film): Film

  fun map(source: Page<dev.pollito.spring_kotlin.sakila.generated.entity.Film>): Page<Film> =
      source.map { map(it) }
}
