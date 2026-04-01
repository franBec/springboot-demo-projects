package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film

interface FilmRepository {
  fun getFilm(id: Int): Film
}
