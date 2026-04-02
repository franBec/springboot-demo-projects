package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.generated.entity.Film
import java.util.Optional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface FilmJpaRepository : JpaRepository<Film, Int> {

  @EntityGraph(attributePaths = ["languageByLanguageId", "languageByOriginalLanguageId"])
  override fun findAll(pageable: Pageable): Page<Film>

  @EntityGraph(attributePaths = ["languageByLanguageId", "languageByOriginalLanguageId"])
  override fun findById(id: Int): Optional<Film>
}
