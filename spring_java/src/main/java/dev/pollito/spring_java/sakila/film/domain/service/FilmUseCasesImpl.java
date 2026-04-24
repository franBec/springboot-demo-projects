package dev.pollito.spring_java.sakila.film.domain.service;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmUseCasesImpl implements FilmUseCases {
  private final FilmRepository repository;

  @Override
  public Film createFilm(Film film) {
    return repository.createFilm(film);
  }

  @Override
  public Film getFilm(Integer id) {
    return repository.getFilm(id);
  }

  @Override
  public Page<Film> getFilms(Pageable pageable) {
    return repository.getFilms(pageable);
  }

  @Override
  public Film updateFilm(Integer id, Film film) {
    return repository.updateFilm(id, film);
  }

  @Override
  public void deleteFilm(Integer id) {
    repository.deleteFilm(id);
  }
}
