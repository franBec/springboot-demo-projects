package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FilmUseCases {
  fun createFilm(film: Film): Film

  fun getFilm(id: Int): Film

  fun getFilms(pageable: Pageable): Page<Film>

  fun updateFilm(id: Int, film: Film): Film
}
