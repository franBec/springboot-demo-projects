package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FindByIdPortOut
import org.springframework.stereotype.Service

@Service
class FindByIdPortInImpl(private val findByIdPortOut: FindByIdPortOut) : FindByIdPortIn {
  override fun findById(id: Int): Film = findByIdPortOut.findById(id)
}
