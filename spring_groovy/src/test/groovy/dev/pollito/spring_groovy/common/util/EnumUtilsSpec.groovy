package dev.pollito.spring_groovy.common.util

import dev.pollito.spring_groovy.common.ValuedEnum
import spock.lang.Specification

class EnumUtilsSpec extends Specification {

  private enum Color implements ValuedEnum<String> {
    RED("red"),
    GREEN("green"),
    BLUE("blue")

    private final String value

    Color(String value) {
      this.value = value
    }

    @Override
    String getValue() {
      value
    }
  }

  def "fromValue returns the matching enum constant"() {
    expect:
    EnumUtils.fromValue(Color, "red") == Color.RED
    EnumUtils.fromValue(Color, "green") == Color.GREEN
    EnumUtils.fromValue(Color, "blue") == Color.BLUE
  }

  def "fromValue throws IllegalArgumentException for unknown value"() {
    when:
    EnumUtils.fromValue(Color, "yellow")

    then:
    IllegalArgumentException ex = thrown()
    ex.message.contains("Color")
    ex.message.contains("yellow")
  }
}
