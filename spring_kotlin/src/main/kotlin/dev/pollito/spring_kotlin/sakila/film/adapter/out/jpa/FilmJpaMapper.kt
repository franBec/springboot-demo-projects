package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.config.mapper.MapperSpringConfig
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import java.time.LocalDate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Page

@Mapper(config = MapperSpringConfig::class)
interface FilmJpaMapper : Converter<dev.pollito.spring_kotlin.generated.entity.Film, Film> {

  @Mapping(target = "id", source = "filmId")
  @Mapping(target = "language", source = "languageByLanguageId.name")
  override fun convert(source: dev.pollito.spring_kotlin.generated.entity.Film): Film

  fun mapReleaseYear(releaseYear: LocalDate?): Int? = releaseYear?.year

  fun convert(source: Page<dev.pollito.spring_kotlin.generated.entity.Film>): Page<Film> =
      source.map { convert(it) }
}
