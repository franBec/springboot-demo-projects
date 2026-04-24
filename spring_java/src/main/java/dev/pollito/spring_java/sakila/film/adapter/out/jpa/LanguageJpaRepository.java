package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.generated.entity.Language;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageJpaRepository extends JpaRepository<Language, Integer> {
  Optional<Language> findByName(String name);
}
