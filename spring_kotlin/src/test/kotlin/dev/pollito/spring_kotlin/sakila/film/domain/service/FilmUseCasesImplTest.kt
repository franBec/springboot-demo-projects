package dev.pollito.spring_kotlin.sakila.film.domain.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FilmUseCasesImplTest {
  @InjectMockKs private lateinit var useCases: FilmUseCasesImpl

  @Test
  fun `getFilm returns a domain model`() {
    assertNotNull(useCases.getFilm(1))
  }
}
