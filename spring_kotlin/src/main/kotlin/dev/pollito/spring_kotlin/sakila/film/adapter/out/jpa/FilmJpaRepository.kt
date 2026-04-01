package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.generated.entity.Film
import org.springframework.data.jpa.repository.JpaRepository

interface FilmJpaRepository : JpaRepository<Film, Int>
