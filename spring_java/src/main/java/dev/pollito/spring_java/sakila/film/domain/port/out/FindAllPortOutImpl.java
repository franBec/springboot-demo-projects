package dev.pollito.spring_java.sakila.film.domain.port.out;

import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaMapper;
import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaRepository;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllPortOutImpl implements FindAllPortOut {
  private final FilmJpaRepository repository;
  private final FilmJpaMapper mapper;

  @Override
  public Page<Film> findAll(Pageable pageable) {
    return mapper.convert(repository.findAll(pageable));
  }
}
