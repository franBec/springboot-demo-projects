package dev.pollito.spring_kotlin.sakila.film.domain.model

import dev.pollito.spring_kotlin.config.enums.ValuedEnum

enum class FilmRating(private val value: String) : ValuedEnum<String> {
  G("G"),
  PG("PG"),
  PG_13("PG-13"),
  R("R"),
  NC_17("NC-17");

  override fun getValue(): String = value
}
