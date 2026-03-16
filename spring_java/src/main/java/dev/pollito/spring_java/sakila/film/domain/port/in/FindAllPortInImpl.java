package dev.pollito.spring_java.sakila.film.domain.port.in;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FindAllPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllPortInImpl implements FindAllPortIn {
  private final FindAllPortOut findAllPortOut;

  @Override
  public Page<Film> findAll(Pageable pageable) {
    return findAllPortOut.findAll(pageable);
  }
}
