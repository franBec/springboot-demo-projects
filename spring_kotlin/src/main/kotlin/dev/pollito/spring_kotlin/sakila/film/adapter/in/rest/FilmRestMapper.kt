package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto.FilmResponse
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel.SPRING

@Mapper(componentModel = SPRING)
interface FilmRestMapper {
  fun map(source: Film): FilmResponse
}
