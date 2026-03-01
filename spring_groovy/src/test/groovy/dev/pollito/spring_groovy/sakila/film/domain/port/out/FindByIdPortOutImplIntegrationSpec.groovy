package dev.pollito.spring_groovy.sakila.film.domain.port.out

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaMapper
import dev.pollito.spring_groovy.sakila.film.domain.model.Film as DomainFilm
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.lang.Unroll

@DataJpaTest
@ActiveProfiles("test")
@Import([FindByIdPortOutImpl, FilmJpaMapper, ModelMapperConfig])
@Sql(scripts = ["/sakila-schema.sql", "/sakila-data.sql"], executionPhase = BEFORE_TEST_CLASS)
class FindByIdPortOutImplIntegrationSpec extends Specification {

  @Autowired
  FindByIdPortOut findByIdPortOut

  @Unroll
  def "findById(#filmId) returns a film with releaseYear=#expectedYear"() {
    expect:
    assertFilm(findByIdPortOut.findById(filmId), filmId, expectedYear)

    where:
    filmId | expectedYear
    1      | 2006
    10     | null
  }

  @CompileStatic
  private static void assertFilm(Object film, int filmId, Integer expectedYear) {
    DomainFilm result = (DomainFilm) film
    assertNotNull(result)
    assertEquals(filmId, result.id)
    assertEquals(expectedYear, result.releaseYear)
  }
}
