---
name: springboot-feature-scaffold
description: Scaffolds or extends a REST domain feature across the spring_java, spring_kotlin, and spring_groovy projects using hexagonal architecture, OpenAPI spec-first workflow, and language-specific idioms.
license: MIT
compatibility: opencode
metadata:
  audience: developers
  workflow: feature-scaffolding
---

## What I do

- Analyze existing domain patterns from the canonical `film` domain across all three projects
- Update OpenAPI specs in all three projects with new paths, schemas, and tags
- Generate or extend domain layer files (models, ports, services) following hexagonal architecture
- Generate or extend adapter layer files (REST controllers, mappers, JPA repositories, repository implementations, JPA mappers)
- Handle language-specific idioms: Java (Lombok + MapStruct + Mockito), Kotlin (data class + MapStruct + MockK + backticks), Groovy (Canonical + ModelMapper + Spock)
- Detect existing partial implementations and complete them (replace `RuntimeException("Not implemented")` stubs)
- Detect missing JPA entities and scaffold only up to the service layer with a clear TODO RuntimeException
- Run `./gradlew :{module}:build` from the monorepo root to verify compilation

## When to use me

Use me when the `/scaffold-feature` command is invoked or when the user asks to:
- Add a new domain (e.g., `actor`, `customer`) to the Sakila API
- Implement missing operations in an existing domain (e.g., finish `createFilm`, `updateFilm`, `deleteFilm`)
- Scaffold a feature following the established patterns

## Instructions

### Step 1: Gather and Normalize Input

1. Extract the **subdomain** argument. Normalize it:
   - Lowercase
   - Singular form (e.g., `actors` -> `actor`)
   - Must be a valid Java identifier (no spaces, no hyphens)
2. Extract the **operations** argument (optional). It is a comma-separated list. Valid values: `list`, `get`, `create`, `update`, `delete`.
3. If subdomain is missing, ask the user for it before proceeding.
4. Determine the **Domain** name (PascalCase, e.g., `actor` -> `Actor`).
5. Determine the **plural** form for REST paths and OpenAPI tags. Use simple pluralization (append `s`). If the plural is irregular (e.g., `category` -> `categories`), ask the user to confirm.

### Step 2: Discovery

For each of the three projects (`spring_java`, `spring_kotlin`, `spring_groovy`), discover the current state:

1. **Check if the domain already exists:**
   - Look for `src/main/{java|kotlin|groovy}/dev/pollito/spring_{lang}/sakila/{subdomain}/`
   - If any files exist, the domain is considered **existing**.

2. **Check if the JPA entity exists:**
   - Look for `build/generated/sources/hibernate/dev/pollito/spring_{lang}/sakila/generated/entity/{Domain}.{java|kt|groovy}`
   - If the file exists, the entity is considered **available**.
   - Note: Groovy and Kotlin projects rename `.java` to `.groovy`/`.kt` during generation. Check for the appropriate extension.

3. **Read canonical patterns from the `film` domain:**
   - Read the existing `film` domain files in **one** project (preferably `spring_java` for clarity) to understand the exact structure:
     - `sakila/film/domain/port/in/FilmUseCases.java`
     - `sakila/film/domain/port/out/FilmRepository.java`
     - `sakila/film/domain/service/FilmUseCasesImpl.java`
     - `sakila/film/adapter/in/rest/FilmRestController.java`
     - `sakila/film/adapter/in/rest/FilmRestMapper.java`
     - `sakila/film/adapter/out/jpa/FilmJpaMapper.java`
     - `sakila/film/adapter/out/jpa/FilmRepositoryImpl.java`
     - `sakila/film/adapter/out/jpa/FilmJpaRepository.java`
     - `sakila/film/domain/model/Film.java`
     - Test files under `src/test/...`
   - Note the exact annotations, imports, method signatures, and naming conventions.
   - Read the corresponding files in `spring_kotlin` and `spring_groovy` to note language-specific differences (backticks, `Int` vs `Integer`, explicit constructors, ModelMapper vs MapStruct, etc.).

4. **Determine the scope:**
   - **New domain**: No existing files found. Default operations = `list,get,create,update,delete` if not provided.
   - **Existing domain**: Files exist. Read the REST controller in all three projects. Identify methods that contain `throw new RuntimeException("Not implemented")` or `throw RuntimeException("Not implemented")`. These are the missing operations. If the user provided operations, intersect them with the missing ones. If no operations provided, default to all missing operations.

### Step 3: OpenAPI Spec Update

For **each** of the three projects, read `src/main/resources/openapi.yaml` and update it.

**If extending an existing domain:**
- Only add the paths and schemas for the newly requested operations.
- If the schemas already exist, do not modify them unless necessary.

**If new domain:**
- Add a new tag under `tags:` (e.g., `- name: Actors`).
- Add paths:
  - `GET /{plural}` — paginated list (`x-spring-paginated: true`), operationId `get{Domain}s`, tag `{Domain}s`
  - `POST /{plural}` — create, operationId `create{Domain}`, tag `{Domain}s`
  - `GET /{plural}/{id}` — get one, operationId `get{Domain}`, tag `{Domain}s`
  - `PUT /{plural}/{id}` — update, operationId `update{Domain}`, tag `{Domain}s`
  - `DELETE /{plural}/{id}` — delete, operationId `delete{Domain}`, tag `{Domain}s`
- Add parameter definition: `{subdomain}Id` (name: `id`, in: `path`, required: true, schema: type integer, minimum 1).
- Add schemas following the exact pattern from `Film`:
  - `{Domain}Fields`: The writable fields object. If JPA entity is available, read it and map its simple fields (String, Integer, etc.) to schema properties. Skip complex relationships and auto-generated fields (`id`, `lastUpdate`). If JPA entity is NOT available, create a minimal `{Domain}Fields` with a single `name` string property as a placeholder.
  - `{Domain}`: `allOf` `{Domain}Fields` + `id` (integer, required) + `lastUpdate` (string, date-time, required).
  - `{Domain}Response`: `allOf` `ResponseMetadata` + `data` property referencing `{Domain}`.
  - `{Domain}ListResponse`: `allOf` `ResponseMetadata` + `data` property that is a `Page` with `content` items referencing `{Domain}`.
  - Any enum schemas derived from entity string fields (if JPA entity available and fields are enums or have `@Enumerated`). Name them `{Domain}{Property}` (e.g., `ActorType`).
- Preserve all existing schemas (`Error`, `Page`, `Pageable`, `ResponseMetadata`, and other domain schemas).

**Critical:** The three `openapi.yaml` files are structurally identical except for the prod server URL (`sakila-java`, `sakila-kotlin`, `sakila-groovy`). Maintain this difference.

### Step 4: Code Generation

For **each** of the three projects, generate or extend files. Use the `film` domain files as the exact template. Replace:
- `Film` -> `{Domain}`
- `film` -> `{subdomain}`
- `films` -> `{plural}`
- `Films` -> `{Domain}s` (for tags/APIs)
- Package paths: `sakila.film` -> `sakila.{subdomain}`

**Language-specific rules:**

| Aspect | Java | Kotlin | Groovy |
|--------|------|--------|--------|
| ID type | `Integer` | `Int` | `Integer` |
| Package `in` | `domain.port.in` | `` domain.port.\`in\` `` | `domain.port.in` |
| Package `in` rest | `adapter.in.rest` | `` adapter.\`in\`.rest `` | `adapter.in.rest` |
| DI | `@RequiredArgsConstructor` | Constructor in class header | Explicit constructor |
| Domain model | `@Data @Builder @NoArgsConstructor @AllArgsConstructor @FieldDefaults(PRIVATE)` | `data class` | `@Canonical @CompileStatic` |
| Mapper | MapStruct `@Mapper(SPRING)` interface | MapStruct `@Mapper(SPRING)` interface | ModelMapper `@Component` class with TypeMaps |
| Service | `@Service @RequiredArgsConstructor` | `@Service` class with header constructor | `@Service @CompileStatic` with explicit constructor |
| Controller | `@RestController @RequiredArgsConstructor` | `@RestController` class with header constructor | `@RestController @CompileStatic` with explicit constructor |
| Tests | JUnit 5, Mockito | JUnit 5 via `kotlin-test-junit5`, MockK | Spock 2.4 |
| *Mocking* | `@MockitoBean` | `@MockkBean` (MockK 1.14.7, springmockk 5.0.1) | `@SpringBean` |
| *Plain unit tests* | `{ClassName}Test.java` | `{ClassName}Test.kt` | `{ClassName}Spec.groovy` |
| *MockMvc slice tests* | `{ClassName}MockMvcTest.java` | `{ClassName}MockMvcTest.kt` | `{ClassName}MockMvcSpec.groovy` |
| *DataJpa slice tests* | `{ClassName}DataJpaTest.java` | `{ClassName}DataJpaTest.kt` | `{ClassName}DataJpaSpec.groovy` |

**For NEW domains, generate these files (if JPA entity IS available):**

1. `domain/model/{Domain}.{java|kt|groovy}`
   - Map fields from JPA entity or use placeholder (`id`, `name`, `lastUpdate`).
   - Java: `@NonNull` on required fields. Kotlin: non-nullable types for required fields, nullable (`?`) for optional. Groovy: no null annotations.

2. `domain/port/in/{Domain}UseCases.{java|kt|groovy}`
   - Methods: `{Domain} get{Domain}(Integer id)` / `(id: Int)` ; `Page<{Domain}> get{Domain}s(Pageable pageable)` / `(pageable: Pageable)`
   - Add `create`, `update`, `delete` methods if those operations are requested.

3. `domain/port/out/{Domain}Repository.{java|kt|groovy}`
   - Mirror the in-port methods.

4. `domain/service/{Domain}UseCasesImpl.{java|kt|groovy}`
   - Implements the in-port.
   - Delegates to the out-port (`{Domain}Repository`).

5. `adapter/in/rest/{Domain}RestController.{java|kt|groovy}`
   - Implements the generated API interface (`{Domain}sApi`).
   - Injects: `{Domain}UseCases`, `{Domain}RestMapper`, `HttpServletRequest`.
   - For **implemented** operations: construct the response envelope using the exact pattern from `FilmRestController` (fluent setters for Java/Groovy, named arguments for Kotlin).
   - For **not-yet-implemented** operations: `throw RuntimeException("Not implemented")`.
   - `delete` returns `ResponseEntity<Void>` (Java/Groovy) or `ResponseEntity<Unit>` (Kotlin) with `204 NO_CONTENT` when implemented.

6. `adapter/in/rest/{Domain}RestMapper.{java|kt|groovy}`
   - Java/Kotlin: MapStruct interface. Must map `Page<{Domain}>` to the generated `{Domain}ListResponseAllOfData`.
   - Kotlin ONLY: If there are enum properties with hyphenated values (e.g., `PG-13`), add explicit `@ValueMappings` in the REST mapper. Java and Groovy do NOT need this.
   - Groovy: ModelMapper `@Component` class with a `map` method and explicit `TypeMap` configuration in constructor if enums exist.

7. `adapter/out/jpa/{Domain}JpaRepository.{java|kt|groovy}`
   - Extends `JpaRepository<GeneratedEntity, Integer>`.

8. `adapter/out/jpa/{Domain}RepositoryImpl.{java|kt|groovy}`
   - Implements `{Domain}Repository`.
   - Injects `{Domain}JpaRepository` and `{Domain}JpaMapper`.

9. `adapter/out/jpa/{Domain}JpaMapper.{java|kt|groovy}`
   - Maps the generated JPA entity to the domain model.
   - Use `EnumUtils.fromValue()` for enum conversions.
   - Handle `OffsetDateTime` conversion from `LocalDateTime` or `Timestamp`.
   - Handle `Year` conversion (e.g., `releaseYear.getYear()`).
   - Java/Kotlin: MapStruct with `@Mapping` annotations and `expression` or `target`/`source`.
   - Groovy: ModelMapper manual mapping or TypeMap configuration.
   - Add a `default` / default method to map `Page<Entity>` -> `Page<Domain>`.

10. **Test files:** Mirror the structure from `film` tests. Follow the exact naming conventions:
    - **Controller test** (`{Domain}RestControllerMockMvcTest` / `{Domain}RestControllerMockMvcSpec`): `@WebMvcTest({Domain}RestController)`, `@Import(ControllerAdvice.class, {Domain}RestMapperImpl.class)` (Java/Kotlin) or `@Import([ControllerAdvice, {Domain}RestMapper, ModelMapperConfig])` (Groovy). Mock the in-port with the correct Spring mock annotation (`@MockitoBean` for Java, `@MockkBean` for Kotlin, `@SpringBean` for Groovy). Test implemented operations for `200 OK` and unimplemented ones for `500 INTERNAL_SERVER_ERROR`.
    - **Service test** (`{Domain}UseCasesImplTest` / `{Domain}UseCasesImplSpec`): Plain unit test. Mock the out-port with Mockito/MockK/Spock. Verify delegation.
    - **Repository test** (`{Domain}RepositoryImplDataJpaTest` / `{Domain}RepositoryImplDataJpaSpec`): `@DataJpaTest` + `@Import({Domain}RepositoryImpl.class, {Domain}JpaMapperImpl.class)` (Java/Kotlin) or `@Import({Domain}RepositoryImpl, {Domain}JpaMapper)` (Groovy). Use `@ActiveProfiles("test")` **and** `@Sql(scripts = {"/sakila-schema.sql", "/sakila-data.sql"}, executionPhase = BEFORE_TEST_CLASS)`.

**Coverage:** JaCoCo requires **80% line** and **50% branch** coverage. Ensure new tests contribute meaningfully. Note that Groovy `config/mapper/**` and domain models are excluded from coverage.

**For NEW domains, if JPA entity is NOT available:**

Generate files 1, 2, 5, 6, and 10 (controller MockMvc test only). **DO NOT generate** files 3, 4, 7, 8, 9, or the repository/service tests.

For the **service implementation** (file 4), generate it but with the following pattern:

```java
@Service
@RequiredArgsConstructor
public class {Domain}UseCasesImpl implements {Domain}UseCases {
  @Override
  public {Domain} get{Domain}(Integer id) {
    throw new RuntimeException("TODO: Domain without existing JPA entities. Outbound Port and its implementation pending");
  }

  @Override
  public Page<{Domain}> get{Domain}s(Pageable pageable) {
    throw new RuntimeException("TODO: Domain without existing JPA entities. Outbound Port and its implementation pending");
  }
  // ... same for create, update, delete if requested
}
```

Use the equivalent Kotlin/Groovy syntax. The REST controller still calls the service; the service throws, which causes a `500 INTERNAL_SERVER_ERROR` handled by the global `ControllerAdvice`.

**For EXISTING domains (extending):**

1. Read the existing controller, in-port, out-port, service, repository impl, and mappers.
2. For each missing operation (identified in Step 2):
   - Add the method signature to the **in-port** interface if missing.
   - Add the method signature to the **out-port** interface if missing AND JPA entity is available.
   - Implement the method in the **service** (delegate to out-port) if JPA entity is available. If NOT available, throw the TODO RuntimeException.
   - Implement the method in the **repository impl** (delegate to JPA repository + mapper) if JPA entity is available.
   - Replace the stub in the **controller** with real response construction.
   - Update **mappers** if new conversions are needed.
3. Update/add **tests** for the newly implemented operations. Use the exact test naming conventions (`MockMvcTest`/`MockMvcSpec`, `Test`/`Spec`, `DataJpaTest`/`DataJpaSpec`) and the correct mocking annotations (`@MockitoBean`, `@MockkBean`, `@SpringBean`).

**Critical Groovy-only rule:** If new domain enums are introduced, update `config/mapper/ModelMapperConfig.groovy` to register custom `AbstractConverter`s for enum conversions (e.g., `{Domain}Language` -> generated `{Domain}Language`). Follow the exact pattern used for `FilmLanguage` and `FilmRating`.

### Step 5: Build Verification

This is a Gradle monorepo. Build each module from the repository root:

- `./gradlew :spring_java:build`
- `./gradlew :spring_kotlin:build`
- `./gradlew :spring_groovy:build`

**Formatting:** Run `./gradlew spotlessApply` before building. The build includes `spotlessCheck`, and generated code will fail if not formatted.

- If the build succeeds, report success.
- If the build fails, capture the errors. Common issues:
  - MapStruct processor not finding generated models: ensure OpenAPI generation task ran before compilation.
  - Kotlin kapt issues: ensure `generateOpenApi` and `generateEntities` are wired to `kaptGenerateStubs`.
  - Missing imports or package declarations in generated files.
  - Enum mapping mismatches between domain and generated models.
- Fix any issues caused by the scaffolding. Do NOT fix pre-existing issues.

### Step 6: Summary

Provide a concise summary to the user:

1. **Subdomain**: `{subdomain}` / `{Domain}`
2. **Operations scaffolded**: list the operations (`list`, `get`, `create`, `update`, `delete`)
3. **JPA entity status**: Available or Missing
4. **Files created/modified per project**: list the new or modified files
5. **Build status**: Success or failures with error count per project
6. **Next steps**:
   - If JPA entity was missing: remind the user to add the table to `sakila-schema.sql`, run `./gradlew generateEntities`, then re-run `/scaffold-feature {subdomain}` to generate the outbound adapters.
   - If placeholder fields were used: remind the user to update the OpenAPI spec, domain model, and mappers to match the actual entity fields.
    - If build failed due to formatting: run `./gradlew spotlessApply` and rebuild.
    - If build failed for other reasons: show the first few errors and suggest fixes.

### Guardrails

- **Never** modify `config/web/ControllerAdvice`, `config/log/*`, `config/enums/*`, or `config/mapper/ModelMapperConfig.groovy` (except to add enum converters for Groovy).
- **Never** duplicate cross-cutting concerns.
- **Preserve** existing code. Only add or replace stubs.
- **Use exact indentation** and style from the `film` domain templates.
- **Do not** create empty commits. The user will review changes via `git diff`.
