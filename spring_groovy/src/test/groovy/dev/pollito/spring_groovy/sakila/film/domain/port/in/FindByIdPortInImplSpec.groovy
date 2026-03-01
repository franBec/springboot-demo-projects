package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FindByIdPortOut
import spock.lang.Specification
import spock.lang.Subject

class FindByIdPortInImplSpec extends Specification {
  FindByIdPortOut findByIdPortOut = Mock()
  @Subject FindByIdPortInImpl findByIdPortIn = new FindByIdPortInImpl(findByIdPortOut)

  def "findById returns a domain model"() {
    given: "a mocked secondary port behavior"
    findByIdPortOut.findById(_ as Integer) >> Stub(Film)

    when: "findById is called"
    def result = findByIdPortIn.findById(1)

    then: "a domain model is returned"
    result != null
  }
}
