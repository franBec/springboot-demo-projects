package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FindByIdPortOut
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FindByIdPortInImpl implements FindByIdPortIn {
  private final FindByIdPortOut findByIdPortOut

  FindByIdPortInImpl(FindByIdPortOut findByIdPortOut) {
    this.findByIdPortOut = findByIdPortOut
  }

  @Override
  Film findById(Integer id) {
    findByIdPortOut.findById(id)
  }
}
