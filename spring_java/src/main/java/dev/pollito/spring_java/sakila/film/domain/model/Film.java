package dev.pollito.spring_java.sakila.film.domain.model;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Film {
  Long id;
  String title;
  String description;
  Integer releaseYear;
  String rating;
  Integer lengthMinutes;
  String language;
}
