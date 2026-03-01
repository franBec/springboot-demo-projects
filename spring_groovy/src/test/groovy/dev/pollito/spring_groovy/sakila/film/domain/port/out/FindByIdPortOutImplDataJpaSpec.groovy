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

@DataJpaTest
@ActiveProfiles("test")
@Import([FindByIdPortOutImpl, FilmJpaMapper, ModelMapperConfig])
@Sql(scripts = ["/sakila-schema.sql", "/sakila-data.sql"], executionPhase = BEFORE_TEST_CLASS)
class FindByIdPortOutImplDataJpaSpec extends Specification {

  @Autowired
  FindByIdPortOut findByIdPortOut

  def "findById finds an Entity and returns a Domain model"() {
    given:
    def id = 1

    expect:
    assertFilm(findByIdPortOut.findById(id), id)
  }

  @CompileStatic
  private static void assertFilm(Object film, int filmId) {
    DomainFilm result = (DomainFilm) film
    assertNotNull(result)
    assertEquals(filmId, result.id)
  }
}