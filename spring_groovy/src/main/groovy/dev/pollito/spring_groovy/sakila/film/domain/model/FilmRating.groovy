package dev.pollito.spring_groovy.sakila.film.domain.model

import dev.pollito.spring_groovy.config.enums.ValuedEnum
import groovy.transform.CompileStatic

@CompileStatic
enum FilmRating implements ValuedEnum<String> {
  G("G"),
  PG("PG"),
  PG_13("PG-13"),
  R("R"),
  NC_17("NC-17")

  private final String value

  FilmRating(String value) {
    this.value = value
  }

  @Override
  String getValue() {
    value
  }
}
