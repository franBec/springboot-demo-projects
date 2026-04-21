package dev.pollito.spring_java.config.enums;

import static dev.pollito.spring_java.config.enums.EnumUtils.fromValue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EnumUtilsTest {

  private enum Color implements ValuedEnum<String> {
    RED("red"),
    GREEN("green"),
    BLUE("blue");

    private final String value;

    Color(String value) {
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }
  }

  @Test
  void fromValue_returnsMatchingConstant() {
    assertEquals(Color.RED, fromValue(Color.class, "red"));
    assertEquals(Color.GREEN, fromValue(Color.class, "green"));
    assertEquals(Color.BLUE, fromValue(Color.class, "blue"));
  }

  @Test
  void fromValue_throwsIllegalArgumentExceptionForUnknownValue() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> fromValue(Color.class, "yellow"));
    assertTrue(ex.getMessage().contains("Color"));
    assertTrue(ex.getMessage().contains("yellow"));
  }
}
