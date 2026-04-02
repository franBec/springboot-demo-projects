package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import static java.time.OffsetDateTime.now
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.config.web.ControllerAdvice
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
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

  private static final String PATH = "/api/films"
  private static final String CONTENT_BODY = """
        {
            "title": "ACADEMY DINOSAUR",
            "language": "English",
            "rentalDuration": 3,
            "rentalRate": 4.99,
            "replacementCost": 20.99
        }
    """

  @Autowired
  MockMvc mockMvc

  @SpringBean
  FilmUseCases useCases = Mock()

  def "getFilm returns OK"() {
    given: "a mocked domain model and primary port behavior"
    def id = 1
    useCases.getFilm(id) >> Stub(Film){getLastUpdate() >> now()}

    when: "getFilm is requested"
    def result = mockMvc.perform(
        get(PATH + "/{id}", id)
        .accept(APPLICATION_JSON)
        )

    then: "response is OK"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields("${PATH}/${id}", OK))
  }

  def "getFilms returns OK"() {
    given: "a mocked use case returning an empty page"
    useCases.getFilms(_ as Pageable) >> new PageImpl<>([], PageRequest.of(0, 10), 0)

    when: "getFilms is requested"
    def result = mockMvc.perform(
        get(PATH)
        .accept(APPLICATION_JSON)
        )

    then: "response is OK"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, OK))
        .andExpect(hasPageFields())
  }

  def "createFilm returns INTERNAL_SERVER_ERROR"() {
    when: "createFilm is requested"
    def result = mockMvc.perform(
        post(PATH)
        .contentType(APPLICATION_JSON)
        .content(CONTENT_BODY)
        .accept(APPLICATION_JSON)
        )

    then: "response is INTERNAL_SERVER_ERROR"
    result
        .andExpect(hasStandardApiResponseFields(PATH, INTERNAL_SERVER_ERROR))
        .andExpect(hasErrorFields(INTERNAL_SERVER_ERROR))
  }

  def "deleteFilm returns INTERNAL_SERVER_ERROR"() {
    given: "an id"
    def id = 1

    when: "deleteFilm is requested"
    def result = mockMvc.perform(
        delete(PATH + "/{id}", id)
        .accept(APPLICATION_JSON)
        )

    then: "response is INTERNAL_SERVER_ERROR"
    result
        .andExpect(hasStandardApiResponseFields("${PATH}/${id}", INTERNAL_SERVER_ERROR))
        .andExpect(hasErrorFields(INTERNAL_SERVER_ERROR))
  }

  def "updateFilm returns INTERNAL_SERVER_ERROR"() {
    given: "an id"
    def id = 1

    when: "updateFilm is requested"
    def result = mockMvc.perform(
        put(PATH + "/{id}", id)
        .contentType(APPLICATION_JSON)
        .content(CONTENT_BODY)
        .accept(APPLICATION_JSON)
        )

    then: "response is INTERNAL_SERVER_ERROR"
    result
        .andExpect(hasStandardApiResponseFields("${PATH}/${id}", INTERNAL_SERVER_ERROR))
        .andExpect(hasErrorFields(INTERNAL_SERVER_ERROR))
  }
}