package dev.pollito.spring_kotlin.sakila.film.adapter.`in`.rest

import com.ninjasquad.springmockk.MockkBean
import dev.pollito.spring_kotlin.config.advice.ControllerAdvice
import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.`in`.FindByIdPortIn
import dev.pollito.spring_kotlin.test.util.hasErrorFields
import dev.pollito.spring_kotlin.test.util.hasStandardApiResponseFields
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(FilmRestController::class)
@Import(ControllerAdvice::class, FilmRestMapperImpl::class)
class FilmRestControllerTest {
  companion object {
    private const val API_FILMS = "/api/films"
  }

  @MockkBean private lateinit var findByIdPortIn: FindByIdPortIn
  @Autowired private lateinit var mockMvc: MockMvc

  @Test
  fun `findById returns OK`() {
    val filmId = 1
    val film = mockk<Film>(relaxed = true)
    every { film.id } returns filmId
    every { findByIdPortIn.findById(filmId) } returns film

    mockMvc
        .get("$API_FILMS/$filmId") { accept = APPLICATION_JSON }
        .andExpect {
          status { isOk() }
          jsonPath("$.data.id") { value(filmId) }
          hasStandardApiResponseFields("$API_FILMS/$filmId", OK)
        }
  }

  @Test
  fun `findById with invalid id returns BAD_REQUEST`() {
    val invalidId = 0L

    mockMvc
        .get("$API_FILMS/$invalidId") { accept = APPLICATION_JSON }
        .andExpect {
          status { isBadRequest() }
          hasStandardApiResponseFields("$API_FILMS/$invalidId", BAD_REQUEST)
          hasErrorFields(BAD_REQUEST)
        }
  }

  @Test
  fun `findAll returns INTERNAL_SERVER_ERROR`() {
    mockMvc
        .get(API_FILMS) { accept = APPLICATION_JSON }
        .andExpect {
          status { isInternalServerError() }
          hasStandardApiResponseFields(API_FILMS, INTERNAL_SERVER_ERROR)
          hasErrorFields(INTERNAL_SERVER_ERROR)
        }
  }
}
