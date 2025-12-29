package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import dev.pollito.spring_java.sakila.film.adapter.in.rest.dto.FilmResponse;
import dev.pollito.spring_java.sakila.film.domain.port.in.FindByIdPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
public class FilmRestController {
  private final FindByIdPortIn findByIdPortIn;
  private final FilmMapper filmMapper;

  @GetMapping("/{id}")
  public FilmResponse findById(@PathVariable Long id) {
    return filmMapper.convert(findByIdPortIn.findById(id));
  }
}
