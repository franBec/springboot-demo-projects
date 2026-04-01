package dev.pollito.spring_java.sakila.film.domain.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FilmUseCasesImplTest {
  @InjectMocks private FilmUseCasesImpl useCases;
  @Mock private FilmRepository repository;

  @Test
  void getFilmReturnsADomainModel() {
    when(repository.getFilm(anyInt())).thenReturn(mock(Film.class));
    assertNotNull(useCases.getFilm(1));
  }

  @Test
  void getFilmsReturnsAPage() {
    when(repository.getFilms(any(Pageable.class)))
        .thenReturn(new PageImpl<>(emptyList(), of(0, 10), 0));
    assertNotNull(useCases.getFilms(of(0, 10)));
  }
}
