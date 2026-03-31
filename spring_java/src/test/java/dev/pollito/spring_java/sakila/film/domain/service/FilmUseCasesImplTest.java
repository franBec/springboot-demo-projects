package dev.pollito.spring_java.sakila.film.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilmUseCasesImplTest {
  @InjectMocks private FilmUseCasesImpl filmUseCases;
  @Mock private FilmRepository repository;

  @Test
  void getFilmReturnsADomainModel() {
    when(repository.getFilm(anyInt())).thenReturn(mock(Film.class));
    assertNotNull(filmUseCases.getFilm(1));
  }
}
