package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static java.util.Objects.isNull;

import dev.pollito.spring_java.sakila.film.adapter.in.rest.dto.FilmResponse;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {
  public FilmResponse convert(Film source) {
    if (isNull(source)) {
      return null;
    }
    return FilmResponse.builder()
        .id(source.getId())
        .title(source.getTitle())
        .description(source.getDescription())
        .releaseYear(source.getReleaseYear())
        .rating(source.getRating())
        .lengthMinutes(source.getLengthMinutes())
        .language(source.getLanguage())
        .build();
  }
}
