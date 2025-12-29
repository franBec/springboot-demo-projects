package dev.pollito.spring_java.sakila.film.domain.service;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import org.springframework.stereotype.Service;

@Service
public class FilmUseCasesImpl implements FilmUseCases {
  @Override
  public Film getFilm(Integer id) {
    return Film.builder()
        .id(id)
        .title("ACADEMY DINOSAUR")
        .description(
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies")
        .releaseYear(2006)
        .rating("PG")
        .length(86)
        .language("English")
        .build();
  }
}
