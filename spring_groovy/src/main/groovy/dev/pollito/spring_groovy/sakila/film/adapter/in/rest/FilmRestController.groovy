package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.adapter.in.rest.dto.FilmResponse
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
import groovy.transform.CompileStatic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/films")
@CompileStatic
class FilmRestController {
  FilmUseCases useCases
  FilmRestMapper mapper

  FilmRestController(FilmUseCases useCases, FilmRestMapper mapper) {
    this.useCases = useCases
    this.mapper = mapper
  }

  @GetMapping("/{id}")
  FilmResponse getFilm(@PathVariable("id") Integer id) {
    mapper.map(useCases.getFilm(id))
  }
}
