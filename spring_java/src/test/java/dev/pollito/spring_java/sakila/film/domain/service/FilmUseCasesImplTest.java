package dev.pollito.spring_java.sakila.film.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilmUseCasesImplTest {
  @InjectMocks private FilmUseCasesImpl useCases;

  @Test
  void getFilmReturnsADomainModel() {
    assertNotNull(useCases.getFilm(1));
  }
}
