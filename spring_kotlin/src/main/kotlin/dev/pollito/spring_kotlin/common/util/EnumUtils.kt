package dev.pollito.spring_kotlin.common.util

import dev.pollito.spring_kotlin.common.ValuedEnum

object EnumUtils {
  fun <E> fromValue(enumClass: Class<E>, value: Any): E where E : Enum<E>, E : ValuedEnum<*> {
    return enumClass.enumConstants.firstOrNull { it.getValue() == value }
        ?: throw IllegalArgumentException("Unknown ${enumClass.simpleName} value: $value")
  }
}
