package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Import({FilmRepositoryImpl.class, FilmJpaMapperImpl.class})
@Sql(
    scripts = {"/sakila-schema.sql", "/sakila-data.sql"},
    executionPhase = BEFORE_TEST_CLASS)
class FilmRepositoryImplDataJpaTest {

  @SuppressWarnings("unused")
  @Autowired
  private FilmRepository repository;

  private static @NonNull Film sampleFilm() {
    return Film.builder()
        .id(1)
        .title("ACADEMY DINOSAUR")
        .description(
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies")
        .releaseYear(2006)
        .rating(FilmRating.PG)
        .length(86)
        .language(FilmLanguage.ENGLISH)
        .originalLanguage(null)
        .rentalDuration(6)
        .rentalRate(BigDecimal.valueOf(0.99))
        .replacementCost(BigDecimal.valueOf(20.99))
        .specialFeatures("Deleted Scenes,Behind the Scenes")
        .lastUpdate(OffsetDateTime.parse("2006-02-15T05:03:42Z"))
        .build();
  }

  static @NonNull Stream<Arguments> getFilmsScenarios() {
    return Stream.of(Arguments.of(of(0, 10), true), Arguments.of(of(1000, 10), false));
  }

  static @NonNull Stream<Arguments> createFilmScenarios() {
    return Stream.of(Arguments.of((FilmLanguage) null), Arguments.of(FilmLanguage.FRENCH));
  }

  @Test
  void getFilmReturnsADomainModel() {
    assertEquals(sampleFilm(), repository.getFilm(1));
  }

  @ParameterizedTest
  @MethodSource("getFilmsScenarios")
  void getFilmsReturnsAPage(Pageable pageable, boolean expectContent) {
    var page = repository.getFilms(pageable);
    assertNotNull(page);
    assertEquals(pageable.getPageSize(), page.getSize());

    if (expectContent) {
      assertFalse(page.isEmpty());
      assertEquals(
          List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
          page.getContent().stream().map(Film::getId).toList());
      assertEquals(sampleFilm(), page.getContent().getFirst());
    } else {
      assertTrue(page.isEmpty());
    }
  }

  @ParameterizedTest
  @MethodSource("createFilmScenarios")
  void createFilmReturnsADomainModel(FilmLanguage originalLanguage) {
    Film film =
        Film.builder()
            .title("NEW FILM")
            .language(FilmLanguage.ENGLISH)
            .originalLanguage(originalLanguage)
            .rentalDuration(3)
            .rentalRate(BigDecimal.valueOf(4.99))
            .replacementCost(BigDecimal.valueOf(20.99))
            .build();
    Film created = repository.createFilm(film);
    assertNotNull(created);
    assertNotNull(created.getId());
    assertEquals("NEW FILM", created.getTitle());
    assertEquals(originalLanguage, created.getOriginalLanguage());
  }

  @Test
  void updateFilmReturnsADomainModel() {
    Film film =
        Film.builder()
            .title("UPDATED FILM")
            .language(FilmLanguage.ENGLISH)
            .rentalDuration(3)
            .rentalRate(BigDecimal.valueOf(4.99))
            .replacementCost(BigDecimal.valueOf(20.99))
            .build();
    Film updated = repository.updateFilm(1, film);
    assertNotNull(updated);
    assertEquals(Integer.valueOf(1), updated.getId());
    assertEquals("UPDATED FILM", updated.getTitle());
  }

  @Test
  void deleteFilmRemovesEntity() {
    repository.deleteFilm(1);
    assertThrows(NoSuchElementException.class, () -> repository.getFilm(1));
  }

  @Test
  void deleteFilmThrowsNoSuchElementExceptionWhenFilmDoesNotExist() {
    assertThrows(NoSuchElementException.class, () -> repository.deleteFilm(999));
  }
}
