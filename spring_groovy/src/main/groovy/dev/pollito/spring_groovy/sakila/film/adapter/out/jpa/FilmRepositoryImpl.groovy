package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FilmRepositoryImpl implements FilmRepository {
  private final FilmJpaRepository repository
  private final FilmJpaMapper mapper

  FilmRepositoryImpl(FilmJpaRepository repository, FilmJpaMapper mapper) {
    this.repository = repository
    this.mapper = mapper
  }

  @Override
  Film getFilm(Integer id) {
    mapper.map(repository.findById(id).orElseThrow())
  }
}
