package dev.pollito.spring_groovy.sakila.film.domain.port.out

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.adapter.out.jpa.FilmJpaMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
@Import([FindAllPortOutImpl, FilmJpaMapper, ModelMapperConfig])
@Sql(scripts = ["/sakila-schema.sql", "/sakila-data.sql"], executionPhase = BEFORE_TEST_CLASS)
class FindAllPortOutImplDataJpaSpec extends Specification {

  @Autowired
  FindAllPortOut findAllPortOut

  def "findAll should return paged results"() {
    when:
    def result = findAllPortOut.findAll(PageRequest.of(0, 10))

    then:
    assertNotNull(result)
    assertFalse(result.isEmpty())
    assertEquals(10, result.numberOfElements)
  }
}
