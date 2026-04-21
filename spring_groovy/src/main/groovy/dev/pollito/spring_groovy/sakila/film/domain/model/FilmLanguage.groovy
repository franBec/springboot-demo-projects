package dev.pollito.spring_groovy.sakila.film.domain.model

import dev.pollito.spring_groovy.config.enums.ValuedEnum
import groovy.transform.CompileStatic

@CompileStatic
enum FilmLanguage implements ValuedEnum<String> {
  ENGLISH("English"),
  ITALIAN("Italian"),
  JAPANESE("Japanese"),
  MANDARIN("Mandarin"),
  FRENCH("French"),
  GERMAN("German")

  private final String value

  FilmLanguage(String value) {
    this.value = value
  }

  @Override
  String getValue() {
    value
  }
}
