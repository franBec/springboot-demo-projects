package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto

data class FilmResponse(
    val id: Long?,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val rating: String,
    val lengthMinutes: Int,
    val language: String,
)
