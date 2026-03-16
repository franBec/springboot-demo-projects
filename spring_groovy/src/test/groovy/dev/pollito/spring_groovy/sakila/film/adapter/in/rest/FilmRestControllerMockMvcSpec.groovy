package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import dev.pollito.spring_groovy.config.advice.ControllerAdvice
import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindAllPortIn
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindByIdPortIn
import dev.pollito.spring_groovy.test.util.MockMvcResultMatchersTrait
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest(FilmRestController)
@Import([ControllerAdvice, FilmRestMapper, ModelMapperConfig])
class FilmRestControllerMockMvcSpec extends Specification implements MockMvcResultMatchersTrait {

  private static final String FILMS_PATH = "/api/films"
  private static final String FILM_BY_ID_TEMPLATE = FILMS_PATH + "/{id}"

  @Autowired
  MockMvc mockMvc

  @SpringBean
  FindByIdPortIn findByIdPortIn = Mock()

  @SpringBean
  FindAllPortIn findAllPortIn = Mock()

  private static String filmPath(Integer id) {
    "${FILMS_PATH}/${id}"
  }

  def "findById returns OK"() {
    given: "a mocked domain model and primary port behavior"
    def filmId = 1
    def film = Stub(Film) {getId() >> filmId}
    findByIdPortIn.findById(filmId) >> film

    when: "findById is requested"
    def result = mockMvc.perform(
        get(FILM_BY_ID_TEMPLATE, filmId)
        .accept(APPLICATION_JSON)
        )

    then: "response is OK"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(filmPath(filmId), OK))
        .andExpect(jsonPath('$.data.id').value(filmId))
  }

  def "findById with invalid id returns BAD_REQUEST"() {
    given: "an invalid film id"
    def invalidId = 0

    when: "findById is requested"
    def result = mockMvc.perform(
        get(FILM_BY_ID_TEMPLATE, invalidId)
        .accept(APPLICATION_JSON)
        )

    then: "response is BAD_REQUEST"
    result
        .andExpect(hasStandardApiResponseFields(filmPath(invalidId), BAD_REQUEST))
        .andExpect(hasErrorFields(BAD_REQUEST))
  }

  def "findAll returns OK"() {
    given: "a mocked findAll port"
    findAllPortIn.findAll(_ as Pageable) >> new PageImpl([], PageRequest.of(0, 20), 0)

    when: "findAll is requested"
    def result = mockMvc.perform(
        get(FILMS_PATH)
        .accept(APPLICATION_JSON)
        )

    then: "response is OK"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(FILMS_PATH, OK))
        .andExpect(hasPageFields())
  }
}
