package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.generated.entity.Film;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmJpaRepository extends JpaRepository<Film, Integer> {

  @EntityGraph(attributePaths = {"languageByLanguageId", "languageByOriginalLanguageId"})
  @NonNull Page<Film> findAll(@NonNull Pageable pageable);

  @EntityGraph(attributePaths = {"languageByLanguageId", "languageByOriginalLanguageId"})
  @NonNull Optional<Film> findById(Integer id);
}
