package dev.pollito.spring_java.sakila.film.domain.service;

import static java.math.BigDecimal.valueOf;
import static java.time.OffsetDateTime.parse;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
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
        .rating(FilmRating.PG)
        .length(86)
        .language(FilmLanguage.ENGLISH)
        .originalLanguage(FilmLanguage.ENGLISH)
        .rentalDuration(3)
        .rentalRate(valueOf(4.99))
        .replacementCost(valueOf(20.99))
        .specialFeatures("Trailers,Deleted Scenes")
        .lastUpdate(parse("2006-02-15T04:03:42Z"))
        .build();
  }
}
