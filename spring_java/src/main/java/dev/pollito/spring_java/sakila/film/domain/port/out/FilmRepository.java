package dev.pollito.spring_java.sakila.film.domain.port.out;

import dev.pollito.spring_java.sakila.film.domain.model.Film;

public interface FilmRepository {
  Film getFilm(Integer id);
}
