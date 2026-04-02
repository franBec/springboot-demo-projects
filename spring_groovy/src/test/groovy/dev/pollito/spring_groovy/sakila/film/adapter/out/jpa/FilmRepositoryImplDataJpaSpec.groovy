package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import static org.springframework.data.domain.PageRequest.of
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles("test")
@Import([FilmRepositoryImpl, FilmJpaMapper, ModelMapperConfig])
@Sql(
scripts = ["/sakila-schema.sql", "/sakila-data.sql"],
executionPhase = BEFORE_TEST_CLASS
)
class FilmRepositoryImplDataJpaSpec extends Specification {

  @Autowired
  FilmRepository repository

  def "getFilm gets an entity and returns a domain model"() {
    when: "getFilm is called"
    Integer id = 1
    def result = repository.getFilm(id)

    then: "a domain model is returned"
    id == result.id
  }

  def "getFilms returns a page"() {
    when: "getFilms is called"
    def result = repository.getFilms(of(0, 10))

    then: "a page is returned with 10 results, with IDs 1 to 10"
    result != null
    result.content.size() == 10
    result.content.collect { it.id } == (1..10).toList()
  }
}
