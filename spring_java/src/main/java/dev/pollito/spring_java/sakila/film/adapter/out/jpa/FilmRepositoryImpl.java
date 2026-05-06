package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmFilter;
import dev.pollito.spring_java.sakila.film.domain.port.out.FilmRepository;
import java.math.BigDecimal;
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

  @Override
  public Page<Film> getFilms(@NonNull FilmFilter filter, Pageable pageable) {
    var search =
        filter.search() != null && !filter.search().isEmpty()
            ? "%" + filter.search() + "%"
            : null;
    var rating = filter.rating() != null ? filter.rating().getValue() : null;
    var language = filter.language() != null ? filter.language().getValue() : null;
    var minLength =
        filter.minLength() != null && filter.minLength() > 0 ? filter.minLength() : null;
    var maxLength =
        filter.maxLength() != null && filter.maxLength() > 0 ? filter.maxLength() : null;
    var minRentalRate =
        filter.minRentalRate() != null
                && filter.minRentalRate().compareTo(BigDecimal.valueOf(0)) > 0
            ? filter.minRentalRate()
            : null;
    var maxRentalRate =
        filter.maxRentalRate() != null
                && filter.maxRentalRate().compareTo(BigDecimal.valueOf(0)) > 0
            ? filter.maxRentalRate()
            : null;
    var releaseYear = filter.releaseYear();
    var page =
        repository.findByFilters(
            search,
            rating,
            language,
            minLength,
            maxLength,
            minRentalRate,
            maxRentalRate,
            releaseYear,
            pageable);
    return mapper.map(page);
  }

  @Override
  public Film updateFilm(Integer id, @NonNull Film film) {
    var language = languageJpaRepository.findByName(film.getLanguage().getValue()).orElseThrow();
    var originalLanguage =
        film.getOriginalLanguage() != null
            ? languageJpaRepository.findByName(film.getOriginalLanguage().getValue()).orElseThrow()
            : null;
    var entity = mapper.map(film, language, originalLanguage);
    entity.setFilmId(id);
    entity.setLastUpdate(java.time.LocalDateTime.now());
    return mapper.map(repository.save(entity));
  }

  @Override
  public void deleteFilm(Integer id) {
    repository.findById(id).orElseThrow();
    repository.deleteById(id);
  }
}
