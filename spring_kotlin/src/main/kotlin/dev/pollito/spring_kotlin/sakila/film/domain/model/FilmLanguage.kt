package dev.pollito.spring_kotlin.sakila.film.domain.model

import dev.pollito.spring_kotlin.config.enums.ValuedEnum

enum class FilmLanguage(private val value: String) : ValuedEnum<String> {
  ENGLISH("English"),
  ITALIAN("Italian"),
  JAPANESE("Japanese"),
  MANDARIN("Mandarin"),
  FRENCH("French"),
  GERMAN("German");

  override fun getValue(): String = value
}
