package dev.pollito.spring_java.sakila.film.domain.model;

import dev.pollito.spring_java.config.enums.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilmLanguage implements ValuedEnum<String> {
  ENGLISH("English"),
  ITALIAN("Italian"),
  JAPANESE("Japanese"),
  MANDARIN("Mandarin"),
  FRENCH("French"),
  GERMAN("German");

  private final String value;
}
