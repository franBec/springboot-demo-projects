package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import java.time.LocalDateTime.now
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FilmRepositoryImpl(
    private val repository: FilmJpaRepository,
    private val mapper: FilmJpaMapper,
    private val languageJpaRepository: LanguageJpaRepository,
) : FilmRepository {
  override fun createFilm(film: Film): Film {
    val language =
        languageJpaRepository.findByName(film.language.getValue()) ?: throw NoSuchElementException()
    val originalLanguage =
        film.originalLanguage?.let {
          languageJpaRepository.findByName(it.getValue()) ?: throw NoSuchElementException()
        }
    val entity = mapper.map(film, language, originalLanguage)
    entity.lastUpdate = now()
    return mapper.map(repository.save(entity))
  }

  override fun getFilm(id: Int): Film = mapper.map(repository.findById(id).orElseThrow())

  override fun getFilms(pageable: Pageable): Page<Film> = mapper.map(repository.findAll(pageable))
}
