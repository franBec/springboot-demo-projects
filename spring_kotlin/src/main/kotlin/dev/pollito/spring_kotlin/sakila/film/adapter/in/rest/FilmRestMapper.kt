package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmRating
import dev.pollito.spring_kotlin.sakila.generated.model.FilmLanguage as GeneratedFilmLanguage
import dev.pollito.spring_kotlin.sakila.generated.model.FilmListResponseAllOfData
import dev.pollito.spring_kotlin.sakila.generated.model.FilmRating as GeneratedFilmRating
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.mapstruct.ValueMapping
import org.mapstruct.ValueMappings
import org.springframework.data.domain.Page

@Mapper(componentModel = SPRING)
interface FilmRestMapper {

  @ValueMappings(
      ValueMapping(source = "ENGLISH", target = "English"),
      ValueMapping(source = "ITALIAN", target = "Italian"),
      ValueMapping(source = "JAPANESE", target = "Japanese"),
      ValueMapping(source = "MANDARIN", target = "Mandarin"),
      ValueMapping(source = "FRENCH", target = "French"),
      ValueMapping(source = "GERMAN", target = "German"),
  )
  fun mapLanguage(source: FilmLanguage): GeneratedFilmLanguage

  @ValueMappings(
      ValueMapping(source = "G", target = "G"),
      ValueMapping(source = "PG", target = "PG"),
      ValueMapping(source = "PG_13", target = "PGMinus13"),
      ValueMapping(source = "R", target = "R"),
      ValueMapping(source = "NC_17", target = "NCMinus17"),
  )
  fun mapRating(source: FilmRating): GeneratedFilmRating

  fun map(source: Film): dev.pollito.spring_kotlin.sakila.generated.model.Film

  @Mapping(
      target = "content",
      expression =
          "java(source.hasContent() ? filmListToFilmList(source.getContent()) : java.util.Collections.emptyList())",
  )
  fun map(source: Page<Film>): FilmListResponseAllOfData

  fun filmListToFilmList(
      source: List<Film>
  ): MutableList<dev.pollito.spring_kotlin.sakila.generated.model.Film>
}
