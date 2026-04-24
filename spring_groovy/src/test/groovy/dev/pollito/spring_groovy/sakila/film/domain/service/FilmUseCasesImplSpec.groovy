package dev.pollito.spring_groovy.sakila.film.domain.service

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

class FilmUseCasesImplSpec extends Specification {
  FilmRepository repository = Mock()
  @Subject FilmUseCasesImpl useCases = new FilmUseCasesImpl(repository)

  def "createFilm returns a domain model"() {
    given: "a mocked repository"
    repository.createFilm(_ as Film) >> Mock(Film)

    when: "createFilm is called"
    def result = useCases.createFilm(new Film())

    then: "a domain model is returned"
    result != null
  }

  def "getFilm returns a domain model"() {
    given: "a mocked repository"
    repository.getFilm(_ as Integer) >> Mock(Film)

    when: "getFilm is called"
    def result = useCases.getFilm(1)

    then: "a domain model is returned"
    result != null
  }

  def "getFilms returns a page"() {
    given: "a mocked repository"
    repository.getFilms(_ as Pageable) >> new PageImpl<>([], PageRequest.of(0, 10), 0)

    when: "getFilms is called"
    def result = useCases.getFilms(PageRequest.of(0, 10))

    then: "a page is returned"
    result != null
  }
}
