package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FindAllPortOut
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FindAllPortInImpl(private val findAllPortOut: FindAllPortOut) : FindAllPortIn {
  override fun findAll(pageable: Pageable): Page<Film> = findAllPortOut.findAll(pageable)
}
