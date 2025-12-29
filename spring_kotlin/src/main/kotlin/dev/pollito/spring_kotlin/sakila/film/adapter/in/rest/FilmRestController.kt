package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto.FilmResponse
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/films")
class FilmRestController(
    private val useCases: FilmUseCases,
    private val mapper: FilmRestMapper,
) {
  @GetMapping("/{id}")
  fun getFilm(@PathVariable id: Int): FilmResponse? {
    return mapper.map(useCases.getFilm(id))
  }
}
