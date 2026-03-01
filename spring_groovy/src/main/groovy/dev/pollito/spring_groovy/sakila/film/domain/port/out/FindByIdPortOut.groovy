package dev.pollito.spring_groovy.sakila.film.domain.port.out

import dev.pollito.spring_groovy.sakila.film.domain.model.Film

interface FindByIdPortOut {
  Film findById(Integer id)
}