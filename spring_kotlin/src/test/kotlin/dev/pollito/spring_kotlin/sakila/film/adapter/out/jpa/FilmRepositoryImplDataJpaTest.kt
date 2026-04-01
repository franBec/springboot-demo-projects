package dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
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

  @Autowired private lateinit var repository: FilmRepository

  @Test
  fun `getFilm gets an entity and returns a domain model`() {
    assertEquals(1, repository.getFilm(1).id)
  }
}
