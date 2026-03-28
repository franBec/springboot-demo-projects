package dev.pollito.spring_groovy.sakila.film.domain.service

import spock.lang.Specification
import spock.lang.Subject

class FilmUseCasesImplSpec extends Specification {
  @Subject FilmUseCasesImpl useCases = new FilmUseCasesImpl()

  def "getFilm returns a domain model"() {
    when: "getFilm is called"
    def result = useCases.getFilm(1)

    then: "a domain model is returned"
    result != null
  }
}
