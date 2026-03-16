package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FindAllPortIn {
  fun findAll(pageable: Pageable): Page<Film>
}
