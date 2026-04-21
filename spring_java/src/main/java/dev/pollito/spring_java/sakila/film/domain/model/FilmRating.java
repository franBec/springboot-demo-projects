package dev.pollito.spring_java.sakila.film.domain.model;

import dev.pollito.spring_java.config.enums.ValuedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilmRating implements ValuedEnum<String> {
  G("G"),
  PG("PG"),
  PG_13("PG-13"),
  R("R"),
  NC_17("NC-17");

  private final String value;
}
