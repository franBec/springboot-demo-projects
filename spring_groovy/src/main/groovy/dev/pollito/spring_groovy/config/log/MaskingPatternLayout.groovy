package dev.pollito.spring_groovy.config.log

import static java.util.regex.Matcher.quoteReplacement
import static java.util.regex.Pattern.CASE_INSENSITIVE
import static java.util.regex.Pattern.MULTILINE
import static java.util.regex.Pattern.compile

import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import groovy.transform.CompileStatic
import java.util.regex.Matcher
import java.util.regex.Pattern

@CompileStatic
class MaskingPatternLayout extends PatternLayout {

  private Pattern multilinePattern
  private final List<String> maskPatterns = []

  void addMaskPattern(String maskPattern) {
    maskPatterns.add(maskPattern)
    multilinePattern =
        compile(maskPatterns.join("|"), MULTILINE | CASE_INSENSITIVE)
  }

  @Override
  String doLayout(ILoggingEvent event) {
    maskMessage(super.doLayout(event))
  }

  private String maskMessage(String message) {
    if (multilinePattern == null) {
      return message
    }

    Matcher matcher = multilinePattern.matcher(message)
    StringBuilder sb = new StringBuilder()

    while (matcher.find()) {
      List<String> groups = (1..matcher.groupCount())
          .collect { matcher.group(it) }
          .findAll { it != null }

      String replacement = groups.empty
          ? matcher.group(0)
          : groups[0] + (groups.size() > 1 ? "****" : "")

      matcher.appendReplacement(sb, quoteReplacement(replacement))
    }
    matcher.appendTail(sb)

    sb.toString()
  }
}
