package dev.pollito.spring_java.sakila.film.domain.port.out;

import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaMapper;
import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaRepository;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdPortOutImpl implements FindByIdPortOut {
  private final FilmJpaRepository repository;
  private final FilmJpaMapper mapper;

  @Override
  public Film findById(Integer id) {
    return mapper.convert(repository.findById(id).orElseThrow());
  }
}
