package dev.pollito.spring_java.sakila.film.domain.port.out;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaMapperImpl;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Import({FindByIdPortOutImpl.class, FilmJpaMapperImpl.class})
@Sql(
    scripts = {"/sakila-schema.sql", "/sakila-data.sql"},
    executionPhase = BEFORE_TEST_CLASS)
class FindByIdPortOutImplIntegrationTest {

  @SuppressWarnings("unused")
  @Autowired
  private FindByIdPortOut findByIdPortOut;

  @ParameterizedTest
  @CsvSource({"1, 2006", "10, "})
  void findById_whenFilmExists_shouldReturnFilm(Integer filmId, Integer expectedYear) {
    Film result = findByIdPortOut.findById(filmId);

    assertNotNull(result);
    assertEquals(filmId, result.getId());
    assertEquals(expectedYear, result.getReleaseYear());
  }
}
