package dev.pollito.spring_java.sakila.film.domain.model;

import java.math.BigDecimal;

public record FilmFilter(
    String search,
    FilmRating rating,
    FilmLanguage language,
    Integer minLength,
    Integer maxLength,
    BigDecimal minRentalRate,
    BigDecimal maxRentalRate,
    Integer releaseYear) {
  public boolean isEmpty() {
    return search == null
        && rating == null
        && language == null
        && minLength == null
        && maxLength == null
        && minRentalRate == null
        && maxRentalRate == null
        && releaseYear == null;
  }
}
