package dev.pollito.spring_groovy.sakila.auth.adapter.in.rest

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_groovy.config.web.ControllerAdvice
import dev.pollito.spring_groovy.sakila.auth.domain.port.in.AuthUseCases
import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest(AuthRestController)
@AutoConfigureMockMvc(addFilters = false)
@Import([ControllerAdvice, AuthRestMapper, ModelMapperConfig])
class AuthRestControllerMockMvcSpec extends Specification {

  private static final String LOGIN_PATH = "/api/auth/login"
  private static final String ME_PATH = "/api/auth/me"

  @Autowired
  MockMvc mockMvc

  @SpringBean
  AuthUseCases authUseCases = Mock()

  @SpringBean
  UserDetailsService userDetailsService = Mock()

  def "login returns token"() {
    given:
    authUseCases.authenticate("Mike", "password") >> "jwt-token-123"

    when:
    def result = mockMvc.perform(
        post(LOGIN_PATH)
        .contentType(APPLICATION_JSON)
        .content('{"username":"Mike","password":"password"}')
        .accept(APPLICATION_JSON)
        )

    then:
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.data.token').value('jwt-token-123'))
  }

  def "getCurrentUserDetails returns user details"() {
    given:
    def staff = new Staff(id: 1, firstName: "Mike", username: "Mike", active: true)
    def sakilaUserDetails = new SakilaUserDetails(staff)
    authUseCases.getCurrentUser() >> sakilaUserDetails

    when:
    def result = mockMvc.perform(get(ME_PATH).accept(APPLICATION_JSON))

    then:
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.data.username').value('Mike'))
        .andExpect(jsonPath('$.data.accountNonLocked').value(true))
        .andExpect(jsonPath('$.data.authorities[0]').value('ROLE_STAFF'))
        .andExpect(jsonPath('$.data.staff.username').value('Mike'))
  }

  def "getCurrentUserDetails handles null user"() {
    given:
    authUseCases.getCurrentUser() >> null

    when:
    def result = mockMvc.perform(get(ME_PATH).accept(APPLICATION_JSON))

    then:
    result.andExpect(status().isOk())
  }
}
