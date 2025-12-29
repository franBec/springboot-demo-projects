package dev.pollito.spring_java.sakila.film.adapter.in.rest.dto;

import static lombok.AccessLevel.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FilmResponse {
  Long id;
  String title;
  String description;
  Integer releaseYear;
  String rating;
  Integer lengthMinutes;
  String language;
}
