package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FindAllPortOut
import groovy.transform.CompileStatic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FindAllPortInImpl implements FindAllPortIn {
  private final FindAllPortOut findAllPortOut

  FindAllPortInImpl(FindAllPortOut findAllPortOut) {
    this.findAllPortOut = findAllPortOut
  }

  @Override
  Page<Film> findAll(Pageable pageable) {
    findAllPortOut.findAll(pageable)
  }
}
