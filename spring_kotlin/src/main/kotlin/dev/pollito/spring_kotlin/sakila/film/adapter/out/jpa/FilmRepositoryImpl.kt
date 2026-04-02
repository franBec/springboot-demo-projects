package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FilmRepositoryImpl(
    private val repository: FilmJpaRepository,
    private val mapper: FilmJpaMapper,
) : FilmRepository {
  override fun getFilm(id: Int): Film = mapper.map(repository.findById(id).orElseThrow())

  override fun getFilms(pageable: Pageable): Page<Film> = mapper.map(repository.findAll(pageable))
}
