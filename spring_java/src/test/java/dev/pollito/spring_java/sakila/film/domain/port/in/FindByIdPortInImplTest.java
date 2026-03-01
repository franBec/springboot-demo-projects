package dev.pollito.spring_java.sakila.film.domain.port.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FindByIdPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindByIdPortInImplTest {
  @InjectMocks private FindByIdPortInImpl findByIdPortIn;
  @Mock private FindByIdPortOut findByIdPortOut;

  @Test
  void findByIdReturnsADomainModel() {
    when(findByIdPortOut.findById(anyInt())).thenReturn(mock(Film.class));
    assertNotNull(findByIdPortIn.findById(1));
  }
}
