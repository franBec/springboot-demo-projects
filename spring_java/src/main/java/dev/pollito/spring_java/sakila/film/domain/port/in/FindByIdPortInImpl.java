package dev.pollito.spring_java.sakila.film.domain.port.in;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FindByIdPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdPortInImpl implements FindByIdPortIn {
  private final FindByIdPortOut findByIdPortOut;

  @Override
  public Film findById(Integer id) {
    return findByIdPortOut.findById(id);
  }
}
