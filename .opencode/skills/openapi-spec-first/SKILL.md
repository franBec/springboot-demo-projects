---
name: openapi-spec-first
description: Generate and implement REST APIs following the OpenAPI spec-first approach with code generation for Spring Boot projects in Java, Kotlin, and Groovy
license: MIT
compatibility: opencode
metadata:
  domain: api-development
  languages: java,kotlin,groovy
  framework: spring-boot
---

## What I Do

- Guide implementation of REST APIs using OpenAPI spec-first methodology
- Help design OpenAPI specifications with proper schema patterns
- Configure OpenAPI Generator plugin for Gradle builds
- Generate API interfaces and model/DTO classes from specs
- Implement controllers that follow generated interfaces

## When to Use Me

Use this skill when:

- Creating new REST endpoints in a Spring Boot project
- Designing OpenAPI specifications for new features
- Configuring OpenAPI code generation in Gradle
- Implementing controllers from generated interfaces
- Converting between domain models and generated DTOs

## Specification Files

### Location

Place OpenAPI specs at `src/main/resources/openapi.yaml`

```yaml
openapi: 3.0.3
info:
  title: API Title
  version: 1.0.0
  description: API description
  contact:
    name: Contact Name
    url: https://example.com

servers:
  - url: http://localhost:8080/api
    description: dev

paths:
  # Endpoint definitions

components:
  schemas:
    # Model definitions
```

## Schema Design Patterns

### Response Metadata Pattern

All responses include metadata:

```yaml
ResponseMetadata:
  type: object
  properties:
    instance:
      type: string
    status:
      type: integer
    timestamp:
      type: string
      format: date-time
    trace:
      type: string
```

### AllOf Composition for Responses

Combine metadata with data:

```yaml
FilmResponse:
  allOf:
    - $ref: '#/components/schemas/ResponseMetadata'
    - type: object
      properties:
        data:
          $ref: '#/components/schemas/SomeSchema'
```

### Operation IDs

Each endpoint requires a unique `operationId`:

```yaml
operationId: findAll      # Generates: findAll()
operationId: findById     # Generates: findById(Long id)
```

## Build Configuration

**Java/Groovy (build.gradle)**:

```groovy
plugins {
    id 'org.openapi.generator' version '7.17.0'
}

openApiGenerate {
    generatorName = "spring"
    inputSpec = layout.projectDirectory.file("src/main/resources/openapi.yaml").asFile.toString()
    outputDir = layout.buildDirectory.dir("generated/sources/openapi").get().asFile.toString()

    def basePackage = "${project.group}.${project.name}.generated".toString()
    apiPackage = "${basePackage}.api"
    modelPackage = "${basePackage}.model"

    configOptions = [
        interfaceOnly             : "true",
        requestMappingMode        : "api_interface",
        skipDefaultInterface      : "true",
        useJakartaEe              : "true",
        useSpringBoot3            : "true",
        useTags                   : "true",
    ]
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated/sources/openapi/src/main/java"))
        }
    }
}

tasks.named('compileJava') {
    dependsOn 'openApiGenerate'
}
```

**Kotlin (build.gradle.kts)**:

```kts
tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenApi") {
    generatorName.set("kotlin-spring")
    inputSpec.set(openApiSpecPath)
    outputDir.set(openApiGeneratedSourcesDir)

    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "modelMutable" to "true",
        "requestMappingMode" to "api_interface",
        "skipDefaultInterface" to "true",
        "useJakartaEe" to "true",
        "useSpringBoot3" to "true",
        "useTags" to "true",
    ))
}

kotlin.sourceSets["main"].kotlin.srcDir("$openApiGeneratedSourcesDir/src/main/kotlin")
tasks.named("compileKotlin") { dependsOn("generateOpenApi") }
```

### Required Dependencies

**Java/Groovy (build.gradle)**:

```groovy
implementation 'io.swagger.core.v3:swagger-annotations:2.2.41'
implementation 'org.openapitools:jackson-databind-nullable:0.2.8'
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

**Kotlin (build.gradle.kts)**:

```kts
val swaggerCoreVersion = "2.2.41"
implementation("io.swagger.core.v3:swagger-annotations:$swaggerCoreVersion")
implementation("io.swagger.core.v3:swagger-models:$swaggerCoreVersion")
implementation("org.springframework.boot:spring-boot-starter-validation")
```

## Controller Implementation

**Java**:

```java
@RestController
@RequiredArgsConstructor
public class FilmRestController implements FilmsApi {
    private final FindByIdPortIn findByIdPortIn;
    private final FilmMapper filmMapper;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<FilmResponse> findById(Long id) {
        return ok(
            new FilmResponse()
                .data(filmMapper.convert(findByIdPortIn.findById(id)))
                .instance(request.getRequestURI())
                .timestamp(now())
                .trace(current().getSpanContext().getTraceId())
                .status(OK.value())
        );
    }
}
```

**Kotlin**:

```kt
@RestController
class FilmRestController(
    private val findByIdPortIn: FindByIdPortIn,
    private val filmMapper: FilmMapper,
    private val request: HttpServletRequest,
) : FilmsApi {
    override fun findById(id: Long): ResponseEntity<FilmResponse> {
        return ok(
            FilmResponse(
                data = filmMapper.convert(findByIdPortIn.findById(id)),
                instance = request.requestURI,
                timestamp = now(),
                trace = current().spanContext.traceId,
                status = OK.value(),
            )
        )
    }
}
```

**Groovy**:

```groovy
@RestController
@CompileStatic
class FilmRestController implements FilmsApi {
    FindByIdPortIn findByIdPortIn
    FilmMapper filmMapper
    HttpServletRequest request

    @Override
    ResponseEntity<FilmResponse> findById(Long id) {
        ok(
            new FilmResponse(
                data: filmMapper.convert(findByIdPortIn.findById(id)),
                instance: request.requestURI,
                timestamp: now(),
                trace: Span.current().spanContext.traceId,
                status: OK.value()
            )
        )
    }
}
```

## Two-Layer Model Approach

1. **Domain Models** - `domain/model/` - Internal business entities, manually maintained
2. **Generated Models** - `generated/model/` - API contract DTOs, auto-generated
3. **Mapping** - Use MapStruct (Java/Kotlin) or ModelMapper (Groovy)

## Adding New Endpoints

1. **Update OpenAPI Spec** with new path and schemas
2. **Run Build**: `./gradlew compileJava` or `compileKotlin`
3. **Implement Controller** by implementing generated interface
4. **Add Domain Logic**: input port, service, mapper

## Cross-Module Differences

| Aspect        | Java              | Kotlin          | Groovy            |
|---------------|-------------------|-----------------|-------------------|
| Generator     | `spring`          | `kotlin-spring` | `spring`          |
| Mapping       | MapStruct         | MapStruct       | ModelMapper       |
| Model Classes | Builder pattern   | Data class      | POJO with builder |
| Testing       | JUnit 5 + Mockito | JUnit 5 + MockK | Spock             |

## Testing Controllers

Controllers are tested using `@WebMvcTest` with mocked dependencies
