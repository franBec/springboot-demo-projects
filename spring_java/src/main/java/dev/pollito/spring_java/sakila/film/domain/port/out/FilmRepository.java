package dev.pollito.spring_java.sakila.film.domain.port.out;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilmRepository {
  Film getFilm(Integer id);

  Page<Film> getFilms(Pageable pageable);
}
