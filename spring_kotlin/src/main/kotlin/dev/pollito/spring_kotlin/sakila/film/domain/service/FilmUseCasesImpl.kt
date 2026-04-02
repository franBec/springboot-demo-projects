package dev.pollito.spring_kotlin.sakila.film.domain.service

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FilmUseCasesImpl(private val repository: FilmRepository) : FilmUseCases {
  override fun getFilm(id: Int): Film = repository.getFilm(id)

  override fun getFilms(pageable: Pageable): Page<Film> = repository.getFilms(pageable)
}
