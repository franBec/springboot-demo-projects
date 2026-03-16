package dev.pollito.spring_groovy.sakila.film.domain.port.out

import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaMapper
import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaRepository
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import groovy.transform.CompileStatic
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FindAllPortOutImpl implements FindAllPortOut {
  private final FilmJpaRepository repository
  private final FilmJpaMapper mapper

  FindAllPortOutImpl(FilmJpaRepository repository, FilmJpaMapper mapper) {
    this.repository = repository
    this.mapper = mapper
  }

  @Override
  Page<Film> findAll(Pageable pageable) {
    mapper.convert(repository.findAll(pageable))
  }
}
