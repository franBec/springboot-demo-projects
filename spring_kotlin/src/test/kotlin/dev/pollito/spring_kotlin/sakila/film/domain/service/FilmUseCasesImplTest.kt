package dev.pollito.spring_kotlin.sakila.film.domain.service

import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FilmUseCasesImplTest {
  @MockK private lateinit var repository: FilmRepository
  @InjectMockKs private lateinit var useCases: FilmUseCasesImpl

  @Test
  fun `getFilm returns a domain model`() {
    every { repository.getFilm(any()) } returns mockk()
    assertNotNull(useCases.getFilm(1))
  }
}
