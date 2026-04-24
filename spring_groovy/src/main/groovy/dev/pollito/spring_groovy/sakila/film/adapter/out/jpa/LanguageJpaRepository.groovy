package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.generated.entity.Language
import org.springframework.data.jpa.repository.JpaRepository

interface LanguageJpaRepository extends JpaRepository<Language, Integer> {
  Optional<Language> findByName(String name)
}
