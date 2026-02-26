# Spring Boot Demo Projects Monorepo

This repository serves as a demo project for [Pollito's Opinion on Spring Boot Development](https://springboot.pollito.tech/) a Spring Boot guide.

## Project Overview

This is a Gradle monorepo containing three Spring Boot 4.x projects demonstrating modern Spring Boot development patterns across different JVM languages:

- **spring_java** - Java 21 with Spring Boot 4.x
- **spring_kotlin** - Kotlin 2.x with Spring Boot 4.x  
- **spring_groovy** - Groovy 5.x with Spring Boot 4.x

All projects share identical architectural patterns but use language-specific idioms and best practices.

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

## Architecture & Conventions

### Package Organization
All projects follow identical structure:
```
dev.pollito.{module}/
├── config/
│   ├── advice/          # Global exception handling
│   ├── log/             # Logging aspects, filters
│   └── mapper/          # Mapping configuration
└── {domain}/
    └── {entity}/
        ├── adapter/in/rest/ # REST controllers
        ├── domain/
        │   ├── model/      # Domain entities (POJOs)
        │   └── port/in/    # Input ports (use cases)
```

### OpenAPI Spec-First Approach
- **API contracts** defined in `src/main/resources/openapi.yaml`
- **Code generation** using OpenAPI Generator plugin
- **Generated code** includes API interfaces and model DTOs with validation
- **Controller implementations** follow generated interfaces
- **Two-layer model approach**: Domain models (manual) ↔ Generated DTOs (auto-generated)

### Mapping Libraries
- **Java & Kotlin**: MapStruct with Spring integration (auto-generated mappings)
- **Groovy**: ModelMapper with manual implementation
- **Configuration**: Spring mapper configuration for consistent mapping behavior

### Error Handling
- **Global exception handling** using `@RestControllerAdvice`
- **RFC 9457 compliance** with standardized error responses
- **Two-tier approach**: Specific handlers for known exceptions + fallback for unexpected errors
- **Consistent response format** with trace correlation for observability

### Testing Conventions
- **Java**: JUnit 5 + Mockito with `@WebMvcTest`
- **Kotlin**: JUnit 5 + MockK with `@WebMvcTest`  
- **Groovy**: Spock Framework with `@SpringBootTest`
- **Custom test utilities** for API response validation
- **Coverage requirements**: 80% line coverage, 50% branch coverage (Java/Kotlin)
- **Mutation testing**: PIT with 70% threshold (Java/Kotlin only)

### Observability
- **OpenTelemetry** for distributed tracing and metrics
- **Prometheus** for metrics collection at `/actuator/prometheus`
- **Loki** for log aggregation with trace correlation
- **Grafana** for visualization with pre-configured dashboards
- **Standardized logging** with trace context and sensitive data masking

## Language-Specific Features

| Feature     | Java Module                                          | Kotlin Module                                 | Groovy Module                                            |
|-------------|------------------------------------------------------|-----------------------------------------------|----------------------------------------------------------|
| **Testing** | JUnit 5 Jupiter with Mockito extension               | JUnit 5 with MockK for idiomatic mocking      | Spock Framework with BDD-style specifications            |
| **Mapping** | MapStruct with auto-generated implementation classes | MapStruct with Kotlin-specific configurations | ModelMapper with manual `@CompileStatic` implementations |
| **Logging** | Lombok `@Slf4j` with standard Java patterns          | Kotlin-logging for idiomatic Kotlin logging   | Groovy `@Slf4j` with dynamic language features           |

## Development Setup

### Local Development
For the best local development experience, I recommend running the projects with their respective `bootRun` commands and using the `dev` profile to avoid OTLP connection noise:

```bash
# Run with dev profile
./gradlew :spring_java:bootRun -Dspring.profiles.active=dev
./gradlew :spring_kotlin:bootRun -Dspring.profiles.active=dev  
./gradlew :spring_groovy:bootRun -Dspring.profiles.active=dev
```

This approach provides several benefits:
- **Clean console output**: Avoids OTLP connection errors that spam your logs
- **Faster startup**: No attempts to connect to non-existent OTLP collector
- **Debug-friendly**: Easy to set breakpoints and debug in your IDE

**Full observability stack (optional):**
If you need the full monitoring stack for development:
1. **Start observability stack**: `docker-compose up -d`
2. **Access monitoring services**:
   - Grafana: http://localhost:3000 (admin/admin)
   - Prometheus: http://localhost:9090
   - Loki: http://localhost:3100
   - Tempo: http://localhost:3200

### Adding New Endpoints
1. Update `src/main/resources/openapi.yaml` with new endpoint definition
2. Run build to generate code: `./gradlew compileJava` (or `compileKotlin`)
3. Implement controller to extend generated interface
4. Add domain logic in `domain/port/in/` and `domain/service/`
5. Update mappings in `config/mapper/`
6. Write tests following module conventions

## Contribution

This project is intended as a companion to the [Pollito's Opinion on Spring Boot Development](https://springboot.pollito.tech/) guide. While direct contributions to this demo repository are not actively sought, feedback on the guide itself is always welcome.

## Skills

For detailed guidance on specific aspects, coding agents can use the OpenCode skills:
- `/skill openapi-spec-first` - OpenAPI-driven development approach
- `/skill mappers` - Mapping library configurations and patterns
- `/skill error-handling` - Global error handling strategies
- `/skill testing-conventions` - Testing frameworks and conventions across languages
- `/skill observability-conventions` - Observability stack and monitoring setup
- `/skill coolify-deployment` - Deployment to VPS using Coolify

Also refer to [AGENTS.md](AGENTS.md) for guidance on working in this monorepo.
