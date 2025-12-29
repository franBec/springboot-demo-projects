package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film

interface FindByIdPortIn {
  fun findById(id: Long): Film
}
