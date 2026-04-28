package dev.pollito.spring_groovy.config.security.userdetails

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import dev.pollito.spring_groovy.sakila.staff.domain.port.out.StaffRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Specification

class SakilaUserDetailsServiceSpec extends Specification {

  def staffRepository = Mock(StaffRepository)
  def sakilaUserDetailsService = new SakilaUserDetailsService(staffRepository)

  def "loadUserByUsername returns user details"() {
    given:
    def staff = new Staff(id: 1, username: "Mike", password: "encoded", firstName: "Mike", lastName: "Hillyer", active: true)
    staffRepository.findByUsername("Mike") >> Optional.of(staff)

    when:
    def userDetails = sakilaUserDetailsService.loadUserByUsername("Mike")

    then:
    userDetails.username == "Mike"
    userDetails.password == "encoded"
    userDetails.accountNonLocked
    ((SakilaUserDetails) userDetails).staff == staff
  }

  def "loadUserByUsername throws UsernameNotFoundException when not found"() {
    given:
    staffRepository.findByUsername("Unknown") >> Optional.empty()

    when:
    sakilaUserDetailsService.loadUserByUsername("Unknown")

    then:
    thrown(UsernameNotFoundException)
  }
}
