package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.config.mapper.MapperSpringConfig
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper(config = MapperSpringConfig::class)
interface FilmMapper : Converter<Film, dev.pollito.spring_kotlin.generated.model.Film> {
  override fun convert(source: Film): dev.pollito.spring_kotlin.generated.model.Film
}
