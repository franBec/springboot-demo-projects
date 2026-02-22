package dev.pollito.spring_groovy.sakila.film.domain.port.in

import spock.lang.Specification
import spock.lang.Subject

class FindByIdPortInImplSpec extends Specification {
  @Subject FindByIdPortInImpl findByIdPortIn = new FindByIdPortInImpl()

  def "findFilmById returns a film with the given id"() {
    given: "a film id"
    def filmId = 1L

    when: "findFilmById is called"
    def result = findByIdPortIn.findById(filmId)

    then: "a film is returned with correct data"
    result != null
    result.id == filmId
  }
}
