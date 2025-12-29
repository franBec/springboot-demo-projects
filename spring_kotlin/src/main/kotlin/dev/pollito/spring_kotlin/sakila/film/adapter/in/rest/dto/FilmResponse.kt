package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest.dto

data class FilmResponse(
    val id: Int,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val rating: String,
    val length: Int,
    val language: String,
)
