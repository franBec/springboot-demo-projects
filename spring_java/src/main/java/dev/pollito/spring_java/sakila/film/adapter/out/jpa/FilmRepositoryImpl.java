package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {
  private final FilmJpaRepository repository;
  private final FilmJpaMapper mapper;

  @Override
  public Film getFilm(Integer id) {
    return mapper.map(repository.findById(id).orElseThrow());
  }
}
