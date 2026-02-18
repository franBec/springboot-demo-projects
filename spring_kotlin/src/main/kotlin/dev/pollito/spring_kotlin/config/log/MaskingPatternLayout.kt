package dev.pollito.spring_kotlin.config.log

import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import kotlin.text.RegexOption.IGNORE_CASE
import kotlin.text.RegexOption.MULTILINE

class MaskingPatternLayout : PatternLayout() {
  private var multilineRegex: Regex? = null
  private val maskPatterns = mutableListOf<String>()

  fun addMaskPattern(maskPattern: String) {
    maskPatterns.add(maskPattern)
    multilineRegex = maskPatterns.joinToString("|").toRegex(setOf(MULTILINE, IGNORE_CASE))
  }

  override fun doLayout(event: ILoggingEvent): String = maskMessage(super.doLayout(event))

  private fun maskMessage(message: String): String {
    val regex = multilineRegex ?: return message

    return regex.replace(message) { match ->
      val nonNullGroups = (1..match.groupValues.lastIndex).mapNotNull { match.groups[it]?.value }

      nonNullGroups.firstOrNull()?.let { prefix ->
        if (nonNullGroups.size >= 2) "$prefix****" else prefix
      } ?: match.value
    }
  }
}
