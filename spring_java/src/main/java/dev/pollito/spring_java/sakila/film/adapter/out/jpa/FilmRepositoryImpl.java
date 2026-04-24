package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {
  private final FilmJpaRepository repository;
  private final FilmJpaMapper mapper;
  private final LanguageJpaRepository languageJpaRepository;

  @Override
  public Film createFilm(@NonNull Film film) {
    var language = languageJpaRepository.findByName(film.getLanguage().getValue()).orElseThrow();
    var originalLanguage =
        film.getOriginalLanguage() != null
            ? languageJpaRepository.findByName(film.getOriginalLanguage().getValue()).orElseThrow()
            : null;
    var entity = mapper.map(film, language, originalLanguage);
    entity.setLastUpdate(java.time.LocalDateTime.now());
    return mapper.map(repository.save(entity));
  }

  @Override
  public Film getFilm(Integer id) {
    return mapper.map(repository.findById(id).orElseThrow());
  }

  @Override
  public Page<Film> getFilms(Pageable pageable) {
    return mapper.map(repository.findAll(pageable));
  }
}
