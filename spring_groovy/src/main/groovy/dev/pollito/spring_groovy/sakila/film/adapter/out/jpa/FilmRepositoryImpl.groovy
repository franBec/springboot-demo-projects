package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.film.domain.model.Film
import dev.pollito.spring_groovy.sakila.film.domain.port.out.FilmRepository
import groovy.transform.CompileStatic
import java.time.LocalDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FilmRepositoryImpl implements FilmRepository {
  private final FilmJpaRepository repository
  private final FilmJpaMapper mapper
  private final LanguageJpaRepository languageJpaRepository

  FilmRepositoryImpl(FilmJpaRepository repository, FilmJpaMapper mapper, LanguageJpaRepository languageJpaRepository) {
    this.repository = repository
    this.mapper = mapper
    this.languageJpaRepository = languageJpaRepository
  }

  @Override
  Film createFilm(Film film) {
    def language = languageJpaRepository.findByName(film.language.value).orElseThrow()
    def originalLanguage = film.originalLanguage ? languageJpaRepository.findByName(film.originalLanguage.value).orElseThrow() : null
    def entity = mapper.map(film, language, originalLanguage)
    entity.lastUpdate = LocalDateTime.now()
    mapper.map(repository.save(entity))
  }

  @Override
  Film getFilm(Integer id) {
    mapper.map(repository.findById(id).orElseThrow())
  }

  @Override
  Page<Film> getFilms(Pageable pageable) {
    mapper.map(repository.findAll(pageable))
  }

  @Override
  Film updateFilm(Integer id, Film film) {
    def language = languageJpaRepository.findByName(film.language.value).orElseThrow()
    def originalLanguage = film.originalLanguage ? languageJpaRepository.findByName(film.originalLanguage.value).orElseThrow() : null
    def entity = mapper.map(film, language, originalLanguage)
    entity.filmId = id
    entity.lastUpdate = LocalDateTime.now()
    mapper.map(repository.save(entity))
  }
}
