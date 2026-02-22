package dev.pollito.spring_groovy.sakila.film.domain.port.in

import spock.lang.Specification
import spock.lang.Subject

class FindByIdPortInImplSpec extends Specification {
  @Subject FindByIdPortInImpl findByIdPortIn = new FindByIdPortInImpl()

  def "findById returns a domain model"() {
    when: "findById is called"
    def result = findByIdPortIn.findById(1)

    then: "a domain model is returned"
    result != null
  }
}
