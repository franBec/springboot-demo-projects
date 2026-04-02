package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FilmRepository {
  fun getFilm(id: Int): Film

  fun getFilms(pageable: Pageable): Page<Film>
}
