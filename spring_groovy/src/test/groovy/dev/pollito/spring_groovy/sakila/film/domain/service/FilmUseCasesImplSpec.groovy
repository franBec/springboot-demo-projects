package dev.pollito.spring_groovy.sakila.film.domain.service

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import spock.lang.Specification
import spock.lang.Subject

class FilmUseCasesImplSpec extends Specification {
  FilmRepository repository = Mock()
  @Subject FilmUseCasesImpl useCases = new FilmUseCasesImpl(repository)

  def "getFilm returns a domain model"() {
    given: "a mocked repository"
    repository.getFilm(_ as Integer) >> Mock(Film)

    when: "getFilm is called"
    def result = useCases.getFilm(1)

    then: "a domain model is returned"
    result != null
  }
}
