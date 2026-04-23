package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static dev.pollito.spring_java.test.util.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.pollito.spring_java.config.web.ControllerAdvice;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

@WebMvcTest(FilmRestController.class)
@Import({ControllerAdvice.class, FilmRestMapperImpl.class})
class FilmRestControllerMockMvcTest {

  private static final String PATH = "/api/films";
  private static final String CONTENT_BODY =
      """
			{
				"title": "ACADEMY DINOSAUR",
				"language": "English",
				"rentalDuration": 3,
				"rentalRate": 4.99,
				"replacementCost": 20.99
			}
			""";

  private static Film sampleFilm(Integer id) {
    return Film.builder()
        .id(id)
        .title("ACADEMY DINOSAUR")
        .description(
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies")
        .releaseYear(2006)
        .rating(FilmRating.PG)
        .length(86)
        .language(FilmLanguage.ENGLISH)
        .originalLanguage(null)
        .rentalDuration(6)
        .rentalRate(BigDecimal.valueOf(0.99))
        .replacementCost(BigDecimal.valueOf(20.99))
        .specialFeatures("Deleted Scenes,Behind the Scenes")
        .lastUpdate(OffsetDateTime.parse("2006-02-15T05:03:42Z"))
        .build();
  }

  private ResultMatcher hasFilmFields(String prefix) {
    return result -> {
      jsonPath(prefix + ".id").value(1).match(result);
      jsonPath(prefix + ".title").value("ACADEMY DINOSAUR").match(result);
      jsonPath(prefix + ".description")
          .value(
              "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies")
          .match(result);
      jsonPath(prefix + ".releaseYear").value(2006).match(result);
      jsonPath(prefix + ".rating").value("PG").match(result);
      jsonPath(prefix + ".length").value(86).match(result);
      jsonPath(prefix + ".language").value("English").match(result);
      jsonPath(prefix + ".rentalDuration").value(6).match(result);
      jsonPath(prefix + ".rentalRate").value(0.99).match(result);
      jsonPath(prefix + ".replacementCost").value(20.99).match(result);
      jsonPath(prefix + ".specialFeatures").value("Deleted Scenes,Behind the Scenes").match(result);
      jsonPath(prefix + ".lastUpdate").value("2006-02-15T05:03:42Z").match(result);
    };
  }

  static List<Page<Film>> getFilmsScenarios() {
    return List.of(
        new PageImpl<>(List.of(sampleFilm(1)), of(0, 10), 1),
        new PageImpl<>(Collections.emptyList(), of(0, 10), 0));
  }

  static @NonNull List<FilmRating> allFilmRatings() {
    return Arrays.asList(FilmRating.values());
  }

  static @NonNull List<FilmLanguage> allFilmLanguages() {
    return Arrays.asList(FilmLanguage.values());
  }

  @SuppressWarnings("unused")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("unused")
  @MockitoBean
  private FilmUseCases filmUseCases;

  @SuppressWarnings("unused")
  @MockitoSpyBean
  private FilmRestMapper mapper;

  @Nested
  @DisplayName("GET /films/{id}")
  class GetFilm {

    @Test
    void returnsOK() throws Exception {
      Integer filmId = 1;
      Film film = sampleFilm(filmId);

      when(filmUseCases.getFilm(anyInt())).thenReturn(film);

      mockMvc
          .perform(get(PATH + "/{id}", filmId).accept(APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().contentType(APPLICATION_JSON))
          .andExpect(hasStandardApiResponseFields(PATH + "/" + filmId, OK))
          .andExpect(hasFilmFields("$.data"));
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_java.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmRatings")
    void mapsAllRatings(FilmRating rating) throws Exception {
      Film film = sampleFilm(1).toBuilder().rating(rating).build();
      when(filmUseCases.getFilm(anyInt())).thenReturn(film);

      mockMvc
          .perform(get(PATH + "/{id}", 1).accept(APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.rating").value(rating.getValue()));
    }

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_java.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#allFilmLanguages")
    void mapsAllLanguages(FilmLanguage language) throws Exception {
      Film film = sampleFilm(1).toBuilder().language(language).build();
      when(filmUseCases.getFilm(anyInt())).thenReturn(film);

      mockMvc
          .perform(get(PATH + "/{id}", 1).accept(APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.language").value(language.getValue()));
    }
  }

  @Nested
  @DisplayName("GET /films")
  class GetFilms {

    @ParameterizedTest
    @MethodSource(
        "dev.pollito.spring_java.sakila.film.adapter.in.rest.FilmRestControllerMockMvcTest#getFilmsScenarios")
    void returnsOK(Page<Film> page) throws Exception {
      when(filmUseCases.getFilms(any(Pageable.class))).thenReturn(page);

      ResultActions actions =
          mockMvc
              .perform(get(PATH).accept(APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(content().contentType(APPLICATION_JSON))
              .andExpect(hasStandardApiResponseFields(PATH, OK))
              .andExpect(hasPageFields())
              .andExpect(jsonPath("$.data.totalElements").value(page.getTotalElements()))
              .andExpect(jsonPath("$.data.totalPages").value(page.getTotalPages()));

      if (page.hasContent()) {
        actions.andExpect(hasFilmFields("$.data.content[0]"));
      }
    }
  }

  @Nested
  @DisplayName("POST /films")
  class CreateFilm {

    @Test
    void returnsInternalServerError() throws Exception {
      HttpStatus status = INTERNAL_SERVER_ERROR;
      mockMvc
          .perform(
              post(PATH)
                  .contentType(APPLICATION_JSON)
                  .content(CONTENT_BODY)
                  .accept(APPLICATION_JSON))
          .andExpect(hasStandardApiResponseFields(PATH, status))
          .andExpect(hasErrorFields(status));
    }
  }

  @Nested
  @DisplayName("DELETE /films/{id}")
  class DeleteFilm {

    @Test
    void returnsInternalServerError() throws Exception {
      Integer filmId = 1;
      HttpStatus status = INTERNAL_SERVER_ERROR;
      mockMvc
          .perform(delete(PATH + "/{id}", filmId).accept(APPLICATION_JSON))
          .andExpect(hasStandardApiResponseFields(PATH + "/" + filmId, status))
          .andExpect(hasErrorFields(status));
    }
  }

  @Nested
  @DisplayName("PUT /films/{id}")
  class UpdateFilm {

    @Test
    void returnsInternalServerError() throws Exception {
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
}
