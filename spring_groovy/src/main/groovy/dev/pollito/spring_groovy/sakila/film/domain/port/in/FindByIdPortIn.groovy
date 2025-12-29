package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.model.Film

interface FindByIdPortIn {
  Film findById(Long id)
}
