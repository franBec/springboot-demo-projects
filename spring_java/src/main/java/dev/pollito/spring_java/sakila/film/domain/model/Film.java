package dev.pollito.spring_java.sakila.film.domain.model;

import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Film {
  Integer id;
  @NonNull String title;
  String description;
  Integer releaseYear;
  FilmRating rating;
  Integer length;
  @NonNull FilmLanguage language;
  FilmLanguage originalLanguage;
  @NonNull Integer rentalDuration;
  @NonNull BigDecimal rentalRate;
  @NonNull BigDecimal replacementCost;
  String specialFeatures;
  OffsetDateTime lastUpdate;
}
