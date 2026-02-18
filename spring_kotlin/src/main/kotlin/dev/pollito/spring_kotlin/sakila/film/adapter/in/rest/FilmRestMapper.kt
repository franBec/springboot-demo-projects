package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.config.mapper.MapperSpringConfig
import dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto.FilmResponse
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper(config = MapperSpringConfig::class)
interface FilmRestMapper : Converter<Film, FilmResponse> {
  override fun convert(source: Film): FilmResponse
}
