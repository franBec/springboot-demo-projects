package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import dev.pollito.spring_groovy.generated.model.Film.RatingEnum
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import org.modelmapper.ModelMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

@ContextConfiguration(classes = [TestModelMapperConfig])
class FilmMapperSpec extends Specification {

  @Subject
  FilmMapper filmMapper

  def setup() {
    filmMapper = new FilmMapper(new ModelMapper())
  }

  def "convert with null input returns null"() {
    expect:
    filmMapper.convert(null) == null
  }

  def "convert with valid film maps correctly using ModelMapper"() {
    given: "a domain film"
    def domainFilm = new Film(
        id: 1L,
        title: "Test Film",
        description: "A test film",
        releaseYear: 2023,
        rating: "PG",
        lengthMinutes: 120,
        language: "English"
        )

    when: "we convert the film"
    def result = filmMapper.convert(domainFilm)

    then: "the result is not null and basic fields are mapped"
    result != null
    result.id == 1L
    result.title == "Test Film"
    result.description == "A test film"
    result.releaseYear == 2023
    result.lengthMinutes == 120
    result.language == "English"

    and: "rating is converted to enum"
    result.rating == RatingEnum.PG
  }

  def "convert handles domain film without rating"() {
    given: "a domain film without rating"
    def domainFilm = new Film(
        id: 5L,
        title: "No Rating Film"
        )

    when: "we convert the film"
    def result = filmMapper.convert(domainFilm)

    then: "the result has no rating issues"
    result.rating == null
    result.id == 5L
    result.title == "No Rating Film"
  }
}

@TestConfiguration
class TestModelMapperConfig {
  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper()
  }
}
