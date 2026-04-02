package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.generated.entity.Film
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface FilmJpaRepository extends JpaRepository<Film, Integer> {

  @EntityGraph(attributePaths = [
    "languageByLanguageId",
    "languageByOriginalLanguageId"
  ])
  Page<Film> findAll(Pageable pageable)

  @EntityGraph(attributePaths = [
    "languageByLanguageId",
    "languageByOriginalLanguageId"
  ])
  Optional<Film> findById(Integer id)
}
