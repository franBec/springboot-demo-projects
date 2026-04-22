package dev.pollito.spring_kotlin.sakila.film.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Film(
    val id: Int?,
    val title: String,
    val description: String?,
    val releaseYear: Int?,
    val rating: FilmRating?,
    val length: Int?,
    val language: FilmLanguage,
    val originalLanguage: FilmLanguage?,
    val rentalDuration: Int,
    val rentalRate: BigDecimal,
    val replacementCost: BigDecimal,
    val specialFeatures: String?,
    val lastUpdate: OffsetDateTime?,
)
