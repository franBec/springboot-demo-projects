package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FilmUseCases {
  Film createFilm(Film film)

  Film getFilm(Integer id)

  Page<Film> getFilms(Pageable pageable)

  Film updateFilm(Integer id, Film film)
}
