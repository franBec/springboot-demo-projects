package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.generated.entity.Language
import org.springframework.data.jpa.repository.JpaRepository

interface LanguageJpaRepository : JpaRepository<Language, Int> {
  fun findByName(name: String): Language?
}
