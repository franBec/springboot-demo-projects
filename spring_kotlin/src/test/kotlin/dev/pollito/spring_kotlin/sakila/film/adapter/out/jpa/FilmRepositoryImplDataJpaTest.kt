package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.film.domain.model.Film
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmLanguage
import dev.pollito.spring_kotlin.sakila.film.domain.model.FilmRating
import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

@DataJpaTest
@ActiveProfiles("test")
@Import(FilmRepositoryImpl::class, FilmJpaMapperImpl::class)
@Sql(
    scripts = ["/sakila-schema.sql", "/sakila-data.sql"],
    executionPhase = BEFORE_TEST_CLASS,
)
class FilmRepositoryImplDataJpaTest {
  companion object {
    @JvmStatic
    fun getFilmsScenarios(): Stream<Arguments> =
        Stream.of(
            Arguments.of(PageRequest.of(0, 10), true),
            Arguments.of(PageRequest.of(1000, 10), false),
        )

    @JvmStatic
    fun createFilmScenarios(): Stream<Arguments> =
        Stream.of(
            Arguments.of(null),
            Arguments.of(FilmLanguage.FRENCH),
        )

    private fun sampleFilm(): Film =
        Film(
            id = 1,
            title = "ACADEMY DINOSAUR",
            description =
                "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies",
            releaseYear = 2006,
            rating = FilmRating.PG,
            length = 86,
            language = FilmLanguage.ENGLISH,
            originalLanguage = null,
            rentalDuration = 6,
            rentalRate = BigDecimal.valueOf(0.99),
            replacementCost = BigDecimal.valueOf(20.99),
            specialFeatures = "Deleted Scenes,Behind the Scenes",
            lastUpdate = OffsetDateTime.parse("2006-02-15T05:03:42Z"),
        )
  }

  @Autowired private lateinit var repository: FilmRepository

  @Test
  fun `getFilm gets an entity and returns a domain model`() {
    assertEquals(sampleFilm(), repository.getFilm(1))
  }

  @ParameterizedTest
  @MethodSource("getFilmsScenarios")
  fun `getFilms returns a page`(pageable: Pageable, expectContent: Boolean) {
    val page = repository.getFilms(pageable)
    assertNotNull(page)
    assertEquals(pageable.pageSize, page.size)

    if (expectContent) {
      assertFalse(page.isEmpty)
      assertEquals(
          listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
          page.content.map { it.id },
      )
      assertEquals(sampleFilm(), page.content.first())
    } else {
      assertTrue(page.isEmpty)
    }
  }

  @ParameterizedTest
  @MethodSource("createFilmScenarios")
  fun `createFilm returns a domain model`(originalLanguage: FilmLanguage?) {
    val film =
        Film(
            id = null,
            title = "NEW FILM",
            description = null,
            releaseYear = null,
            rating = null,
            length = null,
            language = FilmLanguage.ENGLISH,
            originalLanguage = originalLanguage,
            rentalDuration = 3,
            rentalRate = BigDecimal.valueOf(4.99),
            replacementCost = BigDecimal.valueOf(20.99),
            specialFeatures = null,
            lastUpdate = null,
        )
    val created = repository.createFilm(film)
    assertNotNull(created)
    assertNotNull(created.id)
    assertEquals("NEW FILM", created.title)
    assertEquals(originalLanguage, created.originalLanguage)
  }

  @Test
  fun `updateFilm returns a domain model`() {
    val film =
        Film(
            id = null,
            title = "UPDATED FILM",
            description = null,
            releaseYear = null,
            rating = null,
            length = null,
            language = FilmLanguage.ENGLISH,
            originalLanguage = null,
            rentalDuration = 3,
            rentalRate = BigDecimal.valueOf(4.99),
            replacementCost = BigDecimal.valueOf(20.99),
            specialFeatures = null,
            lastUpdate = null,
        )
    val updated = repository.updateFilm(1, film)
    assertNotNull(updated)
    assertEquals(1, updated.id)
    assertEquals("UPDATED FILM", updated.title)
  }
}
