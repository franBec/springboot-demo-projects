package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import com.ninjasquad.springmockk.MockkBean
import dev.pollito.spring_kotlin.config.web.ControllerAdvice
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmRating
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import dev.pollito.spring_kotlin.test.util.hasPageFields
import dev.pollito.spring_kotlin.test.util.hasStandardApiResponseFields
import io.mockk.every
import java.math.BigDecimal
import java.time.OffsetDateTime
import kotlin.test.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(FilmRestController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ControllerAdvice::class, FilmRestMapperImpl::class)
class FilmRestControllerMockMvcTest {
  companion object {
    private const val PATH = "/api/films"

    private fun contentBody(): String = contentBody("English", null)

    private fun contentBody(language: String, rating: String?): String {
      var body =
          "{\"title\":\"ACADEMY DINOSAUR\",\"language\":\"$language\",\"rentalDuration\":3,\"rentalRate\":4.99,\"replacementCost\":20.99"
      if (rating != null) {
        body += ",\"rating\":\"$rating\""
      }
      return "$body}"
    }

    @JvmStatic fun allFilmRatings(): List<FilmRating> = FilmRating.entries

    @JvmStatic fun allFilmLanguages(): List<FilmLanguage> = FilmLanguage.entries

    @JvmStatic
    fun getFilmsScenarios(): List<Page<Film>> =
        listOf(
            PageImpl(listOf(sampleFilm(1)), PageRequest.of(0, 10), 1),
            PageImpl(emptyList(), PageRequest.of(0, 10), 0),
        )

    private fun sampleFilm(id: Int? = null): Film =
        Film(
            id = id,
            title = "ACADEMY DINOSAUR",
            description =
                "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
            releaseYear = 2006,
            rating = FilmRating.PG,
            length = 86,
            language = FilmLanguage.ENGLISH,
            originalLanguage = null,
            rentalDuration = 6,
            rentalRate = BigDecimal.valueOf(0.99),
            replacementCost = BigDecimal.valueOf(20.99),
            specialFeatures = "Deleted Scenes,Behind the Scenes",
            lastUpdate = OffsetDateTime.parse("2006-02-15T05:03:42Z"),
        )
  }

  @MockkBean private lateinit var useCases: FilmUseCases
  @Autowired private lateinit var mockMvc: MockMvc

  @Nested
  @DisplayName("GET /films/{id}")
  inner class GetFilm {

    @Test
    fun `returns OK`() {
      val filmId = 1
      val film = sampleFilm(filmId)
      every { useCases.getFilm(filmId) } returns film

      mockMvc
          .get("$PATH/$filmId") { accept = APPLICATION_JSON }
          .andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            hasStandardApiResponseFields("$PATH/$filmId", OK)
            hasFilmFields("$.data")
          }
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_kotlin.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmRatings"
    )
    fun `maps all ratings`(rating: FilmRating) {
      val film = sampleFilm(1).copy(rating = rating)
      every { useCases.getFilm(1) } returns film

      mockMvc
          .get("$PATH/1") { accept = APPLICATION_JSON }
          .andExpect {
            status { isOk() }
            jsonPath("$.data.rating") { value(rating.getValue()) }
          }
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_kotlin.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmLanguages"
    )
    fun `maps all languages`(language: FilmLanguage) {
      val film = sampleFilm(1).copy(language = language)
      every { useCases.getFilm(1) } returns film

      mockMvc
          .get("$PATH/1") { accept = APPLICATION_JSON }
          .andExpect {
            status { isOk() }
            jsonPath("$.data.language") { value(language.getValue()) }
          }
    }
  }

  @Nested
  @DisplayName("GET /films")
  inner class GetFilms {

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_kotlin.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#getFilmsScenarios"
    )
    fun `returns OK`(page: Page<Film>) {
      every { useCases.getFilms(any<Pageable>()) } returns page

      val actions =
          mockMvc
              .get(PATH) { accept = APPLICATION_JSON }
              .andExpect {
                status { isOk() }
                content { contentType(APPLICATION_JSON) }
                hasStandardApiResponseFields(PATH, OK)
                hasPageFields()
                jsonPath("$.data.totalElements") { value(page.totalElements) }
                jsonPath("$.data.totalPages") { value(page.totalPages) }
              }

      if (page.hasContent()) {
        actions.andExpect { hasFilmFields("$.data.content[0]") }
      }
    }
  }

  @Nested
  @DisplayName("POST /films")
  inner class CreateFilm {

    @Test
    fun `returns CREATED`() {
      every { useCases.createFilm(any()) } returns sampleFilm(1)

      mockMvc
          .post(PATH) {
            contentType = APPLICATION_JSON
            content = contentBody()
            accept = APPLICATION_JSON
          }
          .andExpect {
            status { isCreated() }
            content { contentType(APPLICATION_JSON) }
            hasStandardApiResponseFields(PATH, CREATED)
            hasFilmFields("$.data")
          }
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_kotlin.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmRatings"
    )
    fun `maps all ratings on create`(rating: FilmRating) {
      every { useCases.createFilm(any()) } returns sampleFilm(1)

      mockMvc
          .post(PATH) {
            contentType = APPLICATION_JSON
            content = contentBody("English", rating.getValue())
            accept = APPLICATION_JSON
          }
          .andExpect {
            status { isCreated() }
            content { contentType(APPLICATION_JSON) }
            hasStandardApiResponseFields(PATH, CREATED)
          }
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_kotlin.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmLanguages"
    )
    fun `maps all languages on create`(language: FilmLanguage) {
      every { useCases.createFilm(any()) } returns sampleFilm(1)

      mockMvc
          .post(PATH) {
            contentType = APPLICATION_JSON
            content = contentBody(language.getValue(), null)
            accept = APPLICATION_JSON
          }
          .andExpect {
            status { isCreated() }
            content { contentType(APPLICATION_JSON) }
            hasStandardApiResponseFields(PATH, CREATED)
          }
    }
  }

  @Nested
  @DisplayName("DELETE /films/{id}")
  inner class DeleteFilm {

    @Test
    fun `returns NO_CONTENT`() {
      val filmId = 1
      every { useCases.deleteFilm(filmId) } returns Unit
      mockMvc
          .delete("$PATH/$filmId") { accept = APPLICATION_JSON }
          .andExpect { status { isNoContent() } }
    }
  }

  @Nested
  @DisplayName("PUT /films/{id}")
  inner class UpdateFilm {

    @Test
    fun `returns OK`() {
      val filmId = 1
      every { useCases.updateFilm(any(), any()) } returns sampleFilm(1)

      mockMvc
          .put("$PATH/$filmId") {
            contentType = APPLICATION_JSON
            content = contentBody()
            accept = APPLICATION_JSON
          }
          .andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
            hasStandardApiResponseFields("$PATH/$filmId", OK)
            hasFilmFields("$.data")
          }
    }
  }

  private fun MockMvcResultMatchersDsl.hasFilmFields(prefix: String) {
    jsonPath("$prefix.id") { value(1) }
    jsonPath("$prefix.title") { value("ACADEMY DINOSAUR") }
    jsonPath("$prefix.description") {
      value(
          "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies"
      )
    }
    jsonPath("$prefix.releaseYear") { value(2006) }
    jsonPath("$prefix.rating") { value("PG") }
    jsonPath("$prefix.length") { value(86) }
    jsonPath("$prefix.language") { value("English") }
    jsonPath("$prefix.rentalDuration") { value(6) }
    jsonPath("$prefix.rentalRate") { value(0.99) }
    jsonPath("$prefix.replacementCost") { value(20.99) }
    jsonPath("$prefix.specialFeatures") { value("Deleted Scenes,Behind the Scenes") }
    jsonPath("$prefix.lastUpdate") { value("2006-02-15T05:03:42Z") }
  }
}
