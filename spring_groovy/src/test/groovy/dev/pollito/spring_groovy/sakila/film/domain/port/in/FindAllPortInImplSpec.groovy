package dev.pollito.spring_groovy.sakila.film.domain.port.in

import dev.pollito.spring_groovy.sakila.film.domain.port.out.FindAllPortOut
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

class FindAllPortInImplSpec extends Specification {
  FindAllPortOut findAllPortOut = Mock()
  @Subject FindAllPortInImpl findAllPortIn = new FindAllPortInImpl(findAllPortOut)

  def "findAll returns a page"() {
    given: "a mocked secondary port behavior"
    findAllPortOut.findAll(_ as Pageable) >> new PageImpl([], PageRequest.of(0, 20), 0)

    when: "findAll is called"
    def result = findAllPortIn.findAll(PageRequest.of(0, 20))

    then: "a page is returned"
    result != null
  }
}
