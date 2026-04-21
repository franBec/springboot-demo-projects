package dev.pollito.spring_kotlin.config.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EnumUtilsTest {

  private enum class Color(private val code: String) : ValuedEnum<String> {
    RED("red"),
    GREEN("green"),
    BLUE("blue");

    override fun getValue(): String = code
  }

  private enum class Priority(private val level: Int) : ValuedEnum<Int> {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    override fun getValue(): Int = level
  }

  @Test
  fun `fromValue returns correct enum constant for matching string value`() {
    val result = EnumUtils.fromValue(Color::class.java, "green")
    assertEquals(Color.GREEN, result)
  }

  @Test
  fun `fromValue returns correct enum constant for matching int value`() {
    val result = EnumUtils.fromValue(Priority::class.java, 3)
    assertEquals(Priority.HIGH, result)
  }

  @Test
  fun `fromValue returns first constant when value matches first entry`() {
    val result = EnumUtils.fromValue(Color::class.java, "red")
    assertEquals(Color.RED, result)
  }

  @Test
  fun `fromValue throws IllegalArgumentException for unknown value`() {
    val exception =
        assertThrows(IllegalArgumentException::class.java) {
          EnumUtils.fromValue(Color::class.java, "yellow")
        }
    assertEquals("Unknown Color value: yellow", exception.message)
  }

  @Test
  fun `fromValue exception message includes the enum class name`() {
    val exception =
        assertThrows(IllegalArgumentException::class.java) {
          EnumUtils.fromValue(Priority::class.java, 99)
        }
    assertEquals("Unknown Priority value: 99", exception.message)
  }
}
