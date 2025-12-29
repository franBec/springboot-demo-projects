package dev.pollito.spring_java.sakila.film.domain.port.in;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.springframework.stereotype.Service;

@Service
public class FindByIdPortInImpl implements FindByIdPortIn {
  @Override
  public Film findById(Long id) {
    return Film.builder()
        .id(id)
        .title("ACADEMY DINOSAUR")
        .description(
            "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies")
        .releaseYear(2006)
        .rating("PG")
        .lengthMinutes(86)
        .language("English")
        .build();
  }
}
