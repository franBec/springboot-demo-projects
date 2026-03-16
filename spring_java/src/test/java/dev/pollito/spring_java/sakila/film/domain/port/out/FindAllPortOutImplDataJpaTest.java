package dev.pollito.spring_java.sakila.film.domain.port.out;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import dev.pollito.spring_java.sakila.film.adapter.out.jpa.FilmJpaMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Import({FindAllPortOutImpl.class, FilmJpaMapperImpl.class})
@Sql(
    scripts = {"/sakila-schema.sql", "/sakila-data.sql"},
    executionPhase = BEFORE_TEST_CLASS)
class FindAllPortOutImplDataJpaTest {

  @SuppressWarnings("unused")
  @Autowired
  private FindAllPortOut findAllPortOut;

  @Test
  void findAll_shouldReturnPagedResults() {
    var result = findAllPortOut.findAll(PageRequest.of(0, 10));

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(10, result.getNumberOfElements());
  }
}
