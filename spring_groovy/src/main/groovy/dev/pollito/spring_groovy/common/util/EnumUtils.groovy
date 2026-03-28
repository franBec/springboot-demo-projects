package dev.pollito.spring_groovy.common.util

import dev.pollito.spring_groovy.common.ValuedEnum

class EnumUtils {
  private EnumUtils() {}

  static <E extends Enum<E> & ValuedEnum<V>, V> E fromValue(Class<E> enumClass, V value) {
    E result = enumClass.enumConstants.find { (it as ValuedEnum).getValue() == value } as E
    if (result == null) {
      throw new IllegalArgumentException("Unknown ${enumClass.simpleName} value: ${value}")
    }
    result
  }
}
