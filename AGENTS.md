# AGENTS.md

This guide provides essential information for coding agents.

## Project Structure

This is a Gradle monorepo with three Spring Boot projects:

- `spring_java` - Java 21, Spring Boot 4.x
- `spring_kotlin` - Kotlin 2.x, Spring Boot 4.x
- `spring_groovy` - Groovy 5.x, Spring Boot 4.x

All projects share common patterns but use language-specific idioms.

More information can be found in `README.md`

## Execution Environment

- The environment is running **BusyBox**, which provides a lightweight version of common Unix tools.
- The `pgrep` command is a "stripped down" version and **does not support the `-g` flag**.
- **Lombok LSP support**: The `@franzmoca/opencode-lombok` plugin is configured in `opencode.json` to automatically download `lombok.jar` and configure the JDTLS language server with the Lombok javaagent. If LSP errors like `builder()` or `toBuilder() undefined` still appear, they are likely transient until the plugin downloads and applies Lombok. Gradle build success remains the source of truth.
