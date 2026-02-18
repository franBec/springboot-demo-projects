package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.sakila.film.adapter.in.rest.dto.FilmResponse
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindByIdPortIn
import groovy.transform.CompileStatic
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/films")
@CompileStatic
class FilmRestController {
  FindByIdPortIn findByIdPortIn
  FilmRestMapper mapper

  FilmRestController(FindByIdPortIn findByIdPortIn, FilmRestMapper mapper) {
    this.findByIdPortIn = findByIdPortIn
    this.mapper = mapper
  }

  @GetMapping("/{id}")
  FilmResponse findById(@PathVariable("id") Integer id) {
    mapper.convert(findByIdPortIn.findById(id))
  }
}
