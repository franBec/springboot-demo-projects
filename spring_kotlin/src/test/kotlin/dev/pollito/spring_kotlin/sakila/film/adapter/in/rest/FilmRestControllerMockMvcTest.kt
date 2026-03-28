package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import com.ninjasquad.springmockk.MockkBean
import dev.pollito.spring_kotlin.config.web.ControllerAdvice
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FilmUseCases
import dev.pollito.spring_kotlin.test.util.hasErrorFields
import dev.pollito.spring_kotlin.test.util.hasStandardApiResponseFields
import io.mockk.every
import io.mockk.mockk
import java.time.OffsetDateTime.now
import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(FilmRestController::class)
@Import(ControllerAdvice::class, FilmRestMapperImpl::class)
class FilmRestControllerMockMvcTest {
  companion object {
    private const val PATH = "/api/films"
    private const val CONTENT_BODY =
        """
            {
                "title": "ACADEMY DINOSAUR",
                "language": "English",
                "rentalDuration": 3,
                "rentalRate": 4.99,
                "replacementCost": 20.99
            }
        """
  }

  @MockkBean private lateinit var useCases: FilmUseCases
  @Autowired private lateinit var mockMvc: MockMvc

  @Test
  fun `getFilm returns OK`() {
    val filmId = 1
    val film = mockk<Film>(relaxed = true)
    every { film.lastUpdate } returns now()
    every { useCases.getFilm(filmId) } returns film

    mockMvc
        .get("$PATH/$filmId") { accept = APPLICATION_JSON }
        .andExpect {
          status { isOk() }
          hasStandardApiResponseFields("$PATH/$filmId", OK)
        }
  }

  @Test
  fun `getFilms returns INTERNAL_SERVER_ERROR`() {
    mockMvc
        .get(PATH) { accept = APPLICATION_JSON }
        .andExpect {
          status { isInternalServerError() }
          hasStandardApiResponseFields(PATH, INTERNAL_SERVER_ERROR)
          hasErrorFields(INTERNAL_SERVER_ERROR)
        }
  }

  @Test
  fun `createFilm returns INTERNAL_SERVER_ERROR`() {
    mockMvc
        .post(PATH) {
          contentType = APPLICATION_JSON
          content = CONTENT_BODY
          accept = APPLICATION_JSON
        }
        .andExpect {
          status { isInternalServerError() }
          hasStandardApiResponseFields(PATH, INTERNAL_SERVER_ERROR)
          hasErrorFields(INTERNAL_SERVER_ERROR)
        }
  }

  @Test
  fun `deleteFilm returns INTERNAL_SERVER_ERROR`() {
    val filmId = 1
    mockMvc
        .delete("$PATH/$filmId") { accept = APPLICATION_JSON }
        .andExpect {
          status { isInternalServerError() }
          hasStandardApiResponseFields("$PATH/$filmId", INTERNAL_SERVER_ERROR)
          hasErrorFields(INTERNAL_SERVER_ERROR)
        }
  }

  @Test
  fun `updateFilm returns INTERNAL_SERVER_ERROR`() {
    val filmId = 1
    mockMvc
        .put("$PATH/$filmId") {
          contentType = APPLICATION_JSON
          content = CONTENT_BODY
          accept = APPLICATION_JSON
        }
        .andExpect {
          status { isInternalServerError() }
          hasStandardApiResponseFields("$PATH/$filmId", INTERNAL_SERVER_ERROR)
          hasErrorFields(INTERNAL_SERVER_ERROR)
        }
  }
}
