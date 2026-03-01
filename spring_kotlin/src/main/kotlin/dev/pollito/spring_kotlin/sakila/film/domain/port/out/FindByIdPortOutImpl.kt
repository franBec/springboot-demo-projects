package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaMapper
import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaRepository
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import org.springframework.stereotype.Service

@Service
class FindByIdPortOutImpl(
    private val repository: FilmJpaRepository,
    private val mapper: FilmJpaMapper,
) : FindByIdPortOut {
  override fun findById(id: Int): Film = mapper.convert(repository.findById(id).orElseThrow())
}
