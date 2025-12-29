package dev.pollito.spring_kotlin.sakila.film.domain.model

data class Film(
    val id: Long? = null,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val rating: String,
    val lengthMinutes: Int,
    val language: String,
)
