package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.generated.entity.Film;
import java.math.BigDecimal;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FilmJpaRepository extends JpaRepository<Film, Integer> {

  @EntityGraph(attributePaths = {"languageByLanguageId", "languageByOriginalLanguageId"})
  @NonNull Page<Film> findAll(@NonNull Pageable pageable);

  @EntityGraph(attributePaths = {"languageByLanguageId", "languageByOriginalLanguageId"})
  @NonNull Optional<Film> findById(Integer id);

  @Query(
      "SELECT f FROM Film f "
          + "LEFT JOIN f.languageByLanguageId lbli1_0 "
          + "LEFT JOIN f.languageByOriginalLanguageId lboli1_0 "
          + "WHERE (:search IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :search, '%'))) "
          + "AND (:rating IS NULL OR f.rating = :rating) "
          + "AND (:language IS NULL OR lbli1_0.name = :language) "
          + "AND (:minLength IS NULL OR f.length >= :minLength) "
          + "AND (:maxLength IS NULL OR f.length <= :maxLength) "
          + "AND (:minRentalRate IS NULL OR f.rentalRate >= :minRentalRate) "
          + "AND (:maxRentalRate IS NULL OR f.rentalRate <= :maxRentalRate) "
          + "AND (:releaseYear IS NULL OR f.releaseYear = :releaseYear)")
  Page<Film> findByFilters(
      @Param("search") String search,
      @Param("rating") String rating,
      @Param("language") String language,
      @Param("minLength") Integer minLength,
      @Param("maxLength") Integer maxLength,
      @Param("minRentalRate") BigDecimal minRentalRate,
      @Param("maxRentalRate") BigDecimal maxRentalRate,
      @Param("releaseYear") Integer releaseYear,
      Pageable pageable);
}
