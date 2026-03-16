package dev.pollito.spring_java.sakila.film.domain.port.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.pollito.spring_java.sakila.film.domain.port.out.FindAllPortOut;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindAllPortInImplTest {
  @InjectMocks private FindAllPortInImpl findAllPortIn;
  @Mock private FindAllPortOut findAllPortOut;

  @Test
  void findAllReturnsAPage() {
    when(findAllPortOut.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));
    assertNotNull(findAllPortIn.findAll(PageRequest.of(0, 20)));
  }
}
