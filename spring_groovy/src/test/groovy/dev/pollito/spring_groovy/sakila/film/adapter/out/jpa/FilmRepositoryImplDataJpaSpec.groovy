package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import static org.springframework.data.domain.PageRequest.of
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import java.time.OffsetDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Pageable
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

  private static Film sampleFilm() {
    new Film(
        id: 1,
        title: 'ACADEMY DINOSAUR',
        description: 'A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies',
        releaseYear: 2006,
        rating: FilmRating.PG,
        length: 86,
        language: FilmLanguage.ENGLISH,
        originalLanguage: null,
        rentalDuration: 6,
        rentalRate: BigDecimal.valueOf(0.99),
        replacementCost: BigDecimal.valueOf(20.99),
        specialFeatures: 'Deleted Scenes,Behind the Scenes',
        lastUpdate: OffsetDateTime.parse('2006-02-15T05:03:42Z')
        )
  }

  def "getFilm gets an entity and returns a domain model"() {
    when: "getFilm is called"
    def result = repository.getFilm(1)

    then: "a domain model equal to the expected sample is returned"
    result == sampleFilm()
  }

  def "getFilms returns a page"(Pageable pageable, boolean expectContent) {
    when: "getFilms is called"
    def result = repository.getFilms(pageable)

    then: "a page is returned with expected size and content"
    result != null
    result.size == pageable.pageSize

    if (expectContent) {
      !result.empty
      result.content.collect { it.id } == (1..10).toList()
      result.content.first() == sampleFilm()
    } else {
      result.empty
    }

    where:
    pageable                        | expectContent
    of(0, 10) | true
    of(1000, 10) | false
  }

  def "createFilm returns a domain model"() {
    when: "createFilm is called"
    def film = new Film(
        title: 'NEW FILM',
        language: FilmLanguage.ENGLISH,
        originalLanguage: originalLanguage,
        rentalDuration: 3,
        rentalRate: BigDecimal.valueOf(4.99),
        replacementCost: BigDecimal.valueOf(20.99)
        )
    def result = repository.createFilm(film)

    then: "a domain model with an assigned id is returned"
    result != null
    result.id != null
    result.title == 'NEW FILM'
    result.originalLanguage == originalLanguage

    where:
    originalLanguage << [null, FilmLanguage.FRENCH]
  }
}
