package dev.pollito.spring_groovy.sakila.film.domain.port.out

import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaMapper
import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaRepository
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FindByIdPortOutImpl implements FindByIdPortOut {
  private final FilmJpaRepository repository
  private final FilmJpaMapper mapper

  FindByIdPortOutImpl(FilmJpaRepository repository, FilmJpaMapper mapper) {
    this.repository = repository
    this.mapper = mapper
  }

  @Override
  Film findById(Integer id) {
    mapper.convert(repository.findById(id).orElseThrow())
  }
}
