package dev.pollito.spring_groovy.sakila.film.domain.port.out

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FilmRepository {
  Film getFilm(Integer id)

  Page<Film> getFilms(Pageable pageable)
}
