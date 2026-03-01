package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film

interface FindByIdPortOut {
  fun findById(id: Int): Film
}
