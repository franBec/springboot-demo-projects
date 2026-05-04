package dev.pollito.spring_java.sakila.film.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import dev.pollito.spring_java.sakila.film.domain.port.in.FilmUseCases;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FilmWebController.class)
@AutoConfigureMockMvc(addFilters = false)
class FilmWebControllerMockMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private FilmUseCases filmUseCases;

  private static Film sampleFilm(Integer id) {
    return Film.builder()
        .id(id)
        .title("ACADEMY DINOSAUR")
        .description("A Epic Drama...")
        .releaseYear(2006)
        .rating(FilmRating.PG)
        .length(86)
        .language(FilmLanguage.ENGLISH)
        .rentalDuration(6)
        .rentalRate(BigDecimal.valueOf(0.99))
        .replacementCost(BigDecimal.valueOf(20.99))
        .specialFeatures("Deleted Scenes,Behind the Scenes")
        .lastUpdate(OffsetDateTime.parse("2006-02-15T05:03:42Z"))
        .build();
  }

  @Test
  void listFilmsReturnsListView() throws Exception {
    var page = new PageImpl<>(List.of(sampleFilm(1)), of(0, 10), 1);
    when(filmUseCases.getFilms(any(), any(Pageable.class))).thenReturn(page);

    mockMvc
        .perform(get("/films"))
        .andExpect(status().isOk())
        .andExpect(view().name("films/list"))
        .andExpect(model().attributeExists("films"));
  }

  @Test
  void filmDetailReturnsDetailView() throws Exception {
    when(filmUseCases.getFilm(anyInt())).thenReturn(sampleFilm(1));

    mockMvc
        .perform(get("/films/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("films/detail"))
        .andExpect(model().attribute("film", sampleFilm(1)));
  }

  @Test
  void newFilmFormReturnsFormView() throws Exception {
    mockMvc
        .perform(get("/films/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("films/form"))
        .andExpect(model().attributeExists("film", "isNew", "allLanguages", "allRatings"));
  }

  @Test
  void createFilmRedirectsToDetail() throws Exception {
    when(filmUseCases.createFilm(any())).thenReturn(sampleFilm(1));

    mockMvc
        .perform(
            post("/films")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "New Film")
                .param("language", "ENGLISH")
                .param("rentalDuration", "3")
                .param("rentalRate", "4.99")
                .param("replacementCost", "19.99"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/films/1"));
  }

  @Test
  void editFilmFormReturnsFormView() throws Exception {
    when(filmUseCases.getFilm(anyInt())).thenReturn(sampleFilm(1));

    mockMvc
        .perform(get("/films/{id}/edit", 1))
        .andExpect(status().isOk())
        .andExpect(view().name("films/form"))
        .andExpect(model().attributeExists("film", "isNew", "allLanguages", "allRatings"));
  }

  @Test
  void updateFilmRedirectsToDetail() throws Exception {
    when(filmUseCases.updateFilm(anyInt(), any())).thenReturn(sampleFilm(1));

    mockMvc
        .perform(
            put("/films/{id}", 1)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Updated Title")
                .param("language", "ENGLISH")
                .param("rentalRate", "0.99"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/films/1"));
  }

  @Test
  void deleteFilmRedirectsToList() throws Exception {
    mockMvc
        .perform(delete("/films/{id}", 1))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/films"));
  }
}
