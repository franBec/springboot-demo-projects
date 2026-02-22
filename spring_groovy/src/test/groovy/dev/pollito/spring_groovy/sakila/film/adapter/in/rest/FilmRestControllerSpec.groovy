package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import dev.pollito.spring_groovy.config.advice.ControllerAdvice
import dev.pollito.spring_groovy.config.mapper.ModelMapperConfig
import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FindByIdPortIn
import dev.pollito.spring_groovy.test.util.ApiResponseMatchers
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest(FilmRestController)
@Import([ControllerAdvice, FilmMapper, ModelMapperConfig])
class FilmRestControllerSpec extends Specification implements ApiResponseMatchers {

  private static final String FILMS_PATH = "/api/films"
  private static final String FILM_BY_ID_TEMPLATE = FILMS_PATH + "/{id}"

  @Autowired
  MockMvc mockMvc

  @SpringBean
  FindByIdPortIn findByIdPortIn = Mock()

  private static String filmPath(Long id) {
    "${FILMS_PATH}/${id}"
  }

  def "when findById then returns film"() {
    given: "a film exists"
    def filmId = 1L
    def domainFilm = new Film(
        id: filmId,
        rating: "G"
        )

    and: "the port returns it"
    findByIdPortIn.findById(filmId) >> domainFilm

    when: "we request the film by id"
    def result = mockMvc.perform(
        get(FILM_BY_ID_TEMPLATE, filmId)
        .accept(APPLICATION_JSON)
        )

    then: "we get a successful response with the film"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(filmPath(filmId), OK))
        .andExpect(jsonPath('$.data.id').value(filmId))
  }

  def "when findById with invalid id then returns bad request"() {
    given: "an invalid film id"
    def invalidId = 0L

    when: "we request the film"
    def result = mockMvc.perform(
        get(FILM_BY_ID_TEMPLATE, invalidId)
        .accept(APPLICATION_JSON)
        )

    then: "we get a bad request response"
    result
        .andExpect(hasStandardApiResponseFields(filmPath(invalidId), BAD_REQUEST))
        .andExpect(hasErrorFields(BAD_REQUEST))
  }

  def "when findAll then throws unsupported operation exception"() {
    when: "we request all films"
    def result = mockMvc.perform(
        get(FILMS_PATH)
        .accept(APPLICATION_JSON)
        )

    then: "we get an internal server error"
    result
        .andExpect(hasStandardApiResponseFields(FILMS_PATH, INTERNAL_SERVER_ERROR))
        .andExpect(hasErrorFields(INTERNAL_SERVER_ERROR))
  }
}
