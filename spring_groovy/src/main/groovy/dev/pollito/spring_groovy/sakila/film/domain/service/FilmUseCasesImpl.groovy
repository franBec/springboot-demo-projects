package dev.pollito.spring_groovy.sakila.film.domain.service

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import groovy.transform.CompileStatic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FilmUseCasesImpl implements FilmUseCases {
  private final FilmRepository repository

  FilmUseCasesImpl(FilmRepository repository) {
    this.repository = repository
  }

  @Override
  Film getFilm(Integer id) {
    repository.getFilm(id)
  }

  @Override
  Page<Film> getFilms(Pageable pageable) {
    repository.getFilms(pageable)
  }
}
