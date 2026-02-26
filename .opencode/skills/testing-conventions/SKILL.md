---
name: testing-conventions
description: Testing patterns and conventions for Spring Boot projects using JUnit 5, MockK, and Spock across Java, Kotlin, and Groovy modules
license: MIT
compatibility: opencode
metadata:
  audience: developers
  domain: testing
  languages: java,kotlin,groovy
---

## What I do

- Guide test structure and organization following hexagonal architecture
- Provide language-specific testing patterns for Java (JUnit 5 + Mockito), Kotlin (JUnit 5 + MockK), and Groovy (Spock)
- Enforce naming conventions per language module
- Define custom API response matchers and test utilities
- Configure coverage requirements

## When to use me

Use this skill when:

- Writing new tests for controllers, domain logic, or configuration components
- Setting up test infrastructure for Spring Boot modules
- Reviewing test code for convention compliance
- Configuring test coverage and quality gates

## Frameworks by Module

**Java Module**:

- JUnit 5 with `@ExtendWith(MockitoExtension.class)`
- Mockito via `spring-boot-starter-test`
- `@MockitoBean` for Spring context mocking

**Kotlin Module**:

- JUnit 5 with Kotlin Test
- MockK (`springmockk:5.0.1`, `mockk:1.14.7`)
- `@MockkBean` for Spring context mocking

**Groovy Module**:

- Spock Framework 2.4
- Built-in Spock mocking (no Mockito)
- `@SpringBean` for dependency injection

## Test Structure

```
src/test/{language}/dev/pollito/spring_{language}/
├── config/advice/          # Exception handling tests
├── config/log/             # Logging/observability tests
├── sakila/film/adapter/in/rest/  # REST controller tests
├── sakila/film/domain/port/in/   # Use case tests
├── test/util/              # Custom matchers/helpers
└── {Module}ApplicationTests
```

## Naming Conventions

| Module  | Class Suffix | Method Style                          |
|---------|--------------|---------------------------------------|
| Java    | `*Test`      | `whenAction_thenExpectedBehavior()`   |
| Kotlin  | `*Test`      | `` `when action then expected`() ``   |
| Groovy  | `*Spec`      | `def "when action then expected"()`   |

## Controller Test Pattern

**Java**: `@WebMvcTest` + `@MockitoBean` + `@Import`
**Kotlin**: `@WebMvcTest` + `@MockkBean` + `@Import`
**Groovy**: `@WebMvcTest` + `@SpringBean` + trait mixins

## Domain Test Pattern

**Java**: `@ExtendWith(MockitoExtension.class)` + `@InjectMocks`
**Kotlin**: `@ExtendWith(MockKExtension::class)` + `@InjectMockKs`
**Groovy**: `Specification` + `@Subject` + `given/when/then` blocks

## Coverage Requirements

**JaCoCo** (all modules):

- Line Coverage: Minimum 80%
- Branch Coverage: Minimum 50% (Java/Kotlin)
- Exclusions: Generated code, mappers, domain models

**PIT Mutation Testing** (Java/Kotlin only):

- Mutation Threshold: 70%
- Coverage Threshold: 80%

## Test Utilities

**Java**: `ApiResponseMatchers` utility class
**Kotlin**: Extension functions on `MockMvcResultMatchersDsl`
**Groovy**: Trait mixed into specifications

## Running Tests

```bash
./gradlew test                    # All tests
./gradlew :{module}:test          # Specific module
./gradlew :spring_java:test --tests ClassName  # Single test
```

## Best Practices

1. Tests mirror source code structure
2. Each test is independent with mocked dependencies
3. Clear, descriptive test names following BDD
4. Custom matchers for consistent API response testing
5. Comprehensive coverage requirements enforced