package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaMapper
import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaRepository
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FindAllPortOutImpl(
    private val repository: FilmJpaRepository,
    private val mapper: FilmJpaMapper,
) : FindAllPortOut {
  override fun findAll(pageable: Pageable): Page<Film> =
      mapper.convert(repository.findAll(pageable))
}
