package dev.pollito.spring_kotlin.sakila.film.domain.service

import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FilmRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

@ExtendWith(MockKExtension::class)
class FilmUseCasesImplTest {
  @MockK private lateinit var repository: FilmRepository
  @InjectMockKs private lateinit var useCases: FilmUseCasesImpl

  @Test
  fun `createFilm returns a domain model`() {
    every { repository.createFilm(any()) } returns mockk()
    assertNotNull(useCases.createFilm(mockk()))
  }

  @Test
  fun `getFilm returns a domain model`() {
    every { repository.getFilm(any()) } returns mockk()
    assertNotNull(useCases.getFilm(1))
  }

  @Test
  fun `getFilms returns a Page`() {
    every { repository.getFilms(any()) } returns PageImpl(emptyList(), PageRequest.of(0, 10), 0)
    assertNotNull(useCases.getFilms(PageRequest.of(0, 10)))
  }

  @Test
  fun `updateFilm returns a domain model`() {
    every { repository.updateFilm(any(), any()) } returns mockk()
    assertNotNull(useCases.updateFilm(1, mockk()))
  }

  @Test
  fun `deleteFilm delegates to repository`() {
    every { repository.deleteFilm(any()) } returns Unit
    useCases.deleteFilm(1)
  }

  @Test
  fun `deleteFilm throws NoSuchElementException when film does not exist`() {
    every { repository.deleteFilm(any()) } throws NoSuchElementException()
    assertFailsWith<NoSuchElementException> { useCases.deleteFilm(999) }
  }
}
