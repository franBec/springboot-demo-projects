package dev.pollito.spring_groovy.sakila.film.adapter.in.rest

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
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating
import dev.pollito.spring_groovy.sakila.film.domain.port.in.FilmUseCases
import dev.pollito.spring_groovy.test.util.MockMvcResultMatchersTrait
import java.time.OffsetDateTime
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import spock.lang.Specification

@WebMvcTest(FilmRestController)
@Import([ControllerAdvice, FilmRestMapper, ModelMapperConfig])
class FilmRestControllerMockMvcSpec extends Specification implements MockMvcResultMatchersTrait {

  private static final String PATH = "/api/films"
  private static String contentBody() {
    contentBody('English', null)
  }

  private static String contentBody(String language, String rating) {
    def body = "{\"title\":\"ACADEMY DINOSAUR\",\"language\":\"${language}\",\"rentalDuration\":3,\"rentalRate\":4.99,\"replacementCost\":20.99"
    if (rating != null) {
      body += ",\"rating\":\"${rating}\""
    }
    "${body}}"
  }

  private static Film sampleFilm(Integer id = null) {
    new Film(
        id: id,
        title: 'ACADEMY DINOSAUR',
        description: 'A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies',
        releaseYear: 2006,
        rating: FilmRating.PG,
        length: 86,
        language: FilmLanguage.ENGLISH,
        originalLanguage: null,
        rentalDuration: 6,
        rentalRate: BigDecimal.valueOf(0.99),
        replacementCost: BigDecimal.valueOf(20.99),
        specialFeatures: 'Deleted Scenes,Behind the Scenes',
        lastUpdate: OffsetDateTime.parse('2006-02-15T05:03:42Z')
        )
  }

  private static ResultMatcher hasFilmFields(String prefix) {
    { result ->
      jsonPath("${prefix}.id").value(1).match(result)
      jsonPath("${prefix}.title").value('ACADEMY DINOSAUR').match(result)
      jsonPath("${prefix}.description").value('A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies').match(result)
      jsonPath("${prefix}.releaseYear").value(2006).match(result)
      jsonPath("${prefix}.rating").value('PG').match(result)
      jsonPath("${prefix}.length").value(86).match(result)
      jsonPath("${prefix}.language").value('English').match(result)
      jsonPath("${prefix}.rentalDuration").value(6).match(result)
      jsonPath("${prefix}.rentalRate").value(0.99).match(result)
      jsonPath("${prefix}.replacementCost").value(20.99).match(result)
      jsonPath("${prefix}.specialFeatures").value('Deleted Scenes,Behind the Scenes').match(result)
      jsonPath("${prefix}.lastUpdate").value('2006-02-15T05:03:42Z').match(result)
    } as ResultMatcher
  }

  @Autowired
  MockMvc mockMvc

  @SpringBean
  FilmUseCases useCases = Mock()

  def "getFilm returns OK"() {
    given: "a mocked domain model and primary port behavior"
    def id = 1
    def film = sampleFilm(id)
    useCases.getFilm(id) >> film

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
        .andExpect(hasFilmFields('$.data'))
  }

  def "maps all ratings"(FilmRating rating) {
    given: "a film with each possible rating"
    def film = sampleFilm(1)
    film.rating = rating
    useCases.getFilm(1) >> film

    when: "getFilm is requested"
    def result = mockMvc.perform(get("${PATH}/1").accept(APPLICATION_JSON))

    then: "rating is correctly serialized"
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.data.rating').value(rating.value))

    where:
    rating << FilmRating.values()
  }

  def "maps all languages"(FilmLanguage language) {
    given: "a film with each possible language"
    def film = sampleFilm(1)
    film.language = language
    useCases.getFilm(1) >> film

    when: "getFilm is requested"
    def result = mockMvc.perform(get("${PATH}/1").accept(APPLICATION_JSON))

    then: "language is correctly serialized"
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.data.language').value(language.value))

    where:
    language << FilmLanguage.values()
  }

  def "getFilms returns OK"(PageImpl page) {
    given: "a mocked use case returning a page"
    useCases.getFilms(_ as Pageable) >> page

    when: "getFilms is requested"
    def result = mockMvc.perform(get(PATH).accept(APPLICATION_JSON))

    then: "response is OK with page metadata"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, OK))
        .andExpect(hasPageFields())
        .andExpect(jsonPath('$.data.totalElements').value(page.totalElements))
        .andExpect(jsonPath('$.data.totalPages').value(page.totalPages))

    if (page.hasContent()) {
      result.andExpect(hasFilmFields('$.data.content[0]'))
    }

    where:
    page << [
      new PageImpl<>([sampleFilm(1)], PageRequest.of(0, 10), 1),
      new PageImpl<>([], PageRequest.of(0, 10), 0)
    ]
  }

  def "createFilm returns CREATED"() {
    given: "a mocked use case returning a film"
    useCases.createFilm(_ as Film) >> sampleFilm(1)

    when: "createFilm is requested"
    def result = mockMvc.perform(
        post(PATH)
        .contentType(APPLICATION_JSON)
        .content(contentBody())
        .accept(APPLICATION_JSON)
        )

    then: "response is CREATED"
    result
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, CREATED))
        .andExpect(hasFilmFields('$.data'))
  }

  def "maps all ratings on create"(FilmRating rating) {
    given: "a mocked use case returning a film"
    useCases.createFilm(_ as Film) >> sampleFilm(1)

    when: "createFilm is requested with a specific rating"
    def result = mockMvc.perform(
        post(PATH)
        .contentType(APPLICATION_JSON)
        .content(contentBody('English', rating.value))
        .accept(APPLICATION_JSON)
        )

    then: "response is CREATED"
    result
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, CREATED))

    where:
    rating << FilmRating.values()
  }

  def "maps all languages on create"(FilmLanguage language) {
    given: "a mocked use case returning a film"
    useCases.createFilm(_ as Film) >> sampleFilm(1)

    when: "createFilm is requested with a specific language"
    def result = mockMvc.perform(
        post(PATH)
        .contentType(APPLICATION_JSON)
        .content(contentBody(language.value, null))
        .accept(APPLICATION_JSON)
        )

    then: "response is CREATED"
    result
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, CREATED))

    where:
    language << FilmLanguage.values()
  }

  def "deleteFilm returns NO_CONTENT"() {
    given: "an id"
    def id = 1

    when: "deleteFilm is requested"
    def result = mockMvc.perform(
        delete(PATH + "/{id}", id)
        .accept(APPLICATION_JSON)
        )

    then: "response is NO_CONTENT"
    result.andExpect(status().isNoContent())
  }

  def "updateFilm returns OK"() {
    given: "a mocked use case returning a film"
    def id = 1
    useCases.updateFilm(id, _ as Film) >> sampleFilm(1)

    when: "updateFilm is requested"
    def result = mockMvc.perform(
        put(PATH + "/{id}", id)
        .contentType(APPLICATION_JSON)
        .content(contentBody())
        .accept(APPLICATION_JSON)
        )

    then: "response is OK"
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields("${PATH}/${id}", OK))
        .andExpect(hasFilmFields('$.data'))
  }
}
