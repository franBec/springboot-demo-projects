package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
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

  @Test
  void getFilmReturnsADomainModel() {
    assertNotNull(repository.getFilm(1));
  }

  @Test
  void getFilmsReturnsAPage() {
    assertNotNull(repository.getFilms(of(0, 10)));
  }
}
