package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static dev.pollito.spring_java.test.util.ApiResponseMatchers.hasErrorFields;
import static dev.pollito.spring_java.test.util.ApiResponseMatchers.hasStandardApiResponseFields;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.pollito.spring_java.config.advice.ControllerAdvice;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.in.FindByIdPortIn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FilmRestController.class)
@Import({ControllerAdvice.class, FilmMapperImpl.class})
class FilmRestControllerTest {

  private static final String FILMS_PATH = "/api/films";
  private static final String FILM_BY_ID_TEMPLATE = FILMS_PATH + "/{id}";

  @Autowired private MockMvc mockMvc;
  @MockitoBean private FindByIdPortIn findByIdPortIn;
  @MockitoSpyBean private FilmMapper filmMapper;

  private static String filmPath(Long id) {
    return FILMS_PATH + "/" + id;
  }

  @Test
  void whenFindById_thenReturnsFilm() throws Exception {
    Long filmId = 1L;
    String filmTitle = "Test Film";
    Film domainFilm = mock(Film.class);
    when(domainFilm.getId()).thenReturn(filmId);
    when(domainFilm.getTitle()).thenReturn(filmTitle);

    when(findByIdPortIn.findById(anyLong())).thenReturn(domainFilm);

    mockMvc
        .perform(get(FILM_BY_ID_TEMPLATE, filmId).accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(filmPath(filmId), OK))
        .andExpect(jsonPath("$.data.id").value(filmId))
        .andExpect(jsonPath("$.data.title").value(filmTitle));
  }

  @Test
  void whenFindByIdWithInvalidId_thenReturnsBadRequest() throws Exception {
    Long invalidId = 0L;
    HttpStatus status = BAD_REQUEST;
    mockMvc
        .perform(get(FILM_BY_ID_TEMPLATE, invalidId).accept(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(filmPath(invalidId), status))
        .andExpect(hasErrorFields(status));
  }

  @Test
  void whenFindAll_thenThrowsUnsupportedOperationException() throws Exception {
    HttpStatus status = INTERNAL_SERVER_ERROR;
    mockMvc
        .perform(get(FILMS_PATH).accept(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(FILMS_PATH, status))
        .andExpect(hasErrorFields(status));
  }
}
