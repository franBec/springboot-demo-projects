package dev.pollito.spring_java.sakila.film.domain.port.in;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindByIdPortInImplTest {
  @InjectMocks FindByIdPortInImpl findByIdPortIn;

  @Test
  void shouldReturnFilm_whenFindFilmById() {
    assertNotNull(findByIdPortIn.findById(1L));
  }
}
