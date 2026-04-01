package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static dev.pollito.spring_java.test.util.MockMvcResultMatchers.*;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.pollito.spring_java.config.web.ControllerAdvice;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FilmRestController.class)
@Import({ControllerAdvice.class, FilmRestMapperImpl.class})
class FilmRestControllerMockMvcTest {

  public static final String PATH = "/api/films";
  public static final String CONTENT_BODY =
      """
			{
				"title": "ACADEMY DINOSAUR",
				"language": "English",
				"rentalDuration": 3,
				"rentalRate": 4.99,
				"replacementCost": 20.99
			}
			""";

  @SuppressWarnings("unused")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("unused")
  @MockitoBean
  private FilmUseCases filmUseCases;

  @SuppressWarnings("unused")
  @MockitoSpyBean
  private FilmRestMapper mapper;

  @Test
  void getFilmReturnsOK() throws Exception {
    Integer filmId = 1;
    Film film = mock(Film.class);
    when(film.getId()).thenReturn(filmId);

    when(filmUseCases.getFilm(anyInt())).thenReturn(film);

    mockMvc
        .perform(get(PATH + "/{id}", filmId).accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH + "/" + filmId, OK));
  }

  @Test
  void getFilmsReturnsOK() throws Exception {
    when(filmUseCases.getFilms(any(Pageable.class)))
        .thenReturn(new PageImpl<>(emptyList(), of(0, 10), 0));

    mockMvc
        .perform(get(PATH).accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, OK))
        .andExpect(hasPageFields());
  }

  @Test
  void createFilmReturnsINTERNAL_SERVER_ERROR() throws Exception {
    HttpStatus status = INTERNAL_SERVER_ERROR;
    mockMvc
        .perform(
            post(PATH).contentType(APPLICATION_JSON).content(CONTENT_BODY).accept(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH, status))
        .andExpect(hasErrorFields(status));
  }

  @Test
  void deleteFilmReturnsINTERNAL_SERVER_ERROR() throws Exception {
    Integer filmId = 1;
    HttpStatus status = INTERNAL_SERVER_ERROR;
    mockMvc
        .perform(delete(PATH + "/{id}", filmId).accept(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH + "/" + filmId, status))
        .andExpect(hasErrorFields(status));
  }

  @Test
  void updateFilmReturnsINTERNAL_SERVER_ERROR() throws Exception {
    Integer filmId = 1;
    HttpStatus status = INTERNAL_SERVER_ERROR;
    mockMvc
        .perform(
            put(PATH + "/{id}", filmId)
                .contentType(APPLICATION_JSON)
                .content(CONTENT_BODY)
                .accept(APPLICATION_JSON))
        .andExpect(hasStandardApiResponseFields(PATH + "/" + filmId, status))
        .andExpect(hasErrorFields(status));
  }
}
