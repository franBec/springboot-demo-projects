package dev.pollito.spring_java.sakila.film.domain.port.in;

import dev.pollito.spring_java.sakila.film.domain.model.Film;

public interface FindByIdPortIn {
  Film findById(Integer id);
}
