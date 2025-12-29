package dev.pollito.spring_kotlin.sakila.film.domain.model

data class Film(
    val id: Int,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val rating: String,
    val length: Int,
    val language: String,
)
