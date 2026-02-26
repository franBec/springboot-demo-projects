# AGENTS.md

This guide provides essential information for agentic coding agents working in this Spring Boot demo projects monorepo.

## Project Structure

This is a Gradle monorepo with three Spring Boot projects:

- `spring_java` - Java 21, Spring Boot 4.x
- `spring_kotlin` - Kotlin 2.x, Spring Boot 4.x
- `spring_groovy` - Groovy 5.x, Spring Boot 4.x

All projects share common patterns but use language-specific idioms.

## Essential Commands

### Build Commands

- `./gradlew build` - Full build: formats code, compiles, tests, runs coverage & mutation testing
- `./gradlew :spring_java:build` - Build only Java module
- `./gradlew :spring_groovy:build` - Build only Groovy module
- `./gradlew :spring_kotlin:build` - Build only Kotlin module

### Run Tests

- `./gradlew test` - Run all tests across all modules
- `./gradlew :{module}:test` - Run tests for specific module
- `./gradlew :spring_java:test --tests dev.pollito.spring_java.sakila.film.adapter.in.rest.FilmRestControllerTest` - Run single test (Java example)

### Code Formatting

- `./gradlew spotlessCheck` - Check code formatting
- `./gradlew spotlessApply` - Apply code formatting (Formatting is applied automatically during `./gradlew build`)

## Package Organization

All projects follow identical structure:

```
dev.pollito.{module}/
├── config/
│   ├── advice/          # Global exception handling
│   ├── log/             # Logging aspects, filters
│   ├── mapper/          # Mapping configuration
└── {domain}/
    └── {entity}/
        ├── adapter/in/rest/ # REST controllers
        ├── domain/
        │   ├── model/      # Domain entities (POJOs)
        │   └── port/in/    # Input ports (use cases)
```

## Execution Environment

- The environment is running **BusyBox**, which provides a lightweight version of common Unix tools.
- The `pgrep` command is a "stripped down" version and **does not support the `-g` flag**.
