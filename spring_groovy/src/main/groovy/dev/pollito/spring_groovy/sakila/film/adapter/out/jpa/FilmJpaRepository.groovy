package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.generated.entity.Film
import org.springframework.data.jpa.repository.JpaRepository

interface FilmJpaRepository extends JpaRepository<Film, Integer> {}
