package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.config.mapper.MapperSpringConfig
import dev.pollito.spring_kotlin.generated.model.FilmListResponseAllOfData
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Page

@Mapper(config = MapperSpringConfig::class)
interface FilmRestMapper : Converter<Film, dev.pollito.spring_kotlin.generated.model.Film> {
  override fun convert(source: Film): dev.pollito.spring_kotlin.generated.model.Film

  fun filmListToFilmList(list: List<Film>): List<dev.pollito.spring_kotlin.generated.model.Film>

  @Mapping(
      target = "content",
      expression =
          "java(source.hasContent() ? filmListToFilmList(source.getContent()) : java.util.Collections.emptyList())",
  )
  fun convert(source: Page<Film>): FilmListResponseAllOfData
}
