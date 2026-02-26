---
name: mappers
description: Conventions for mapping domain entities to DTOs using MapStruct (Java/Kotlin) and ModelMapper (Groovy) in this Spring Boot monorepo
license: MIT
compatibility: opencode
metadata:
  domain: spring-boot
  scope: monorepo
---

## What I do

- Guide creation of mapper classes following project conventions
- Provide correct dependencies for each module (Java, Kotlin, Groovy)
- Show proper configuration patterns for MapStruct and ModelMapper
- Define naming and package structure conventions
- Demonstrate testing patterns for mappers

## When to use me

Use this skill when:

- Creating new mappers to convert domain entities to DTOs
- Adding mapping dependencies to a module
- Configuring MapStruct or ModelMapper
- Writing tests for mapper implementations
- Understanding the differences between Java/Kotlin (MapStruct) and Groovy (ModelMapper) approaches

## Library Dependencies

**Java**: MapStruct + Spring Extensions
```gradle
implementation "org.mapstruct:mapstruct:1.6.3"
annotationProcessor "org.mapstruct:mapstruct-processor:1.6.3"
implementation "org.mapstruct.extensions.spring:mapstruct-spring-annotations:2.0.0"
annotationProcessor "org.mapstruct.extensions.spring:mapstruct-spring-extensions:2.0.0"
```

**Kotlin**: MapStruct + Spring Extensions
```kt
implementation("org.mapstruct:mapstruct:1.6.3")
kapt("org.mapstruct:mapstruct-processor:1.6.3")
implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:2.0.0")
```

**Groovy**: ModelMapper
```gradle
implementation 'org.modelmapper:modelmapper:3.2.6'
```

## Configuration

**Java**:
```java
@MapperConfig(componentModel = "spring")
@SpringMapperConfig(conversionServiceAdapterPackage = "dev.pollito.spring_java.config.mapper")
public interface MapperSpringConfig {}
```

**Kotlin**:
```kt
@MapperConfig(componentModel = "spring")
@SpringMapperConfig(conversionServiceAdapterPackage = "dev.pollito.spring_kotlin.config.mapper")
interface MapperSpringConfig {}
```

**Groovy**:
```groovy
@Configuration
@CompileStatic
class ModelMapperConfig {
  @Bean
  ModelMapper modelMapper() {
    new ModelMapper()
  }
}
```

## Naming Conventions

- **Classes**: `XxxMapper` (e.g., `FilmMapper`)
- **Generated implementations**: `XxxMapperImpl` (MapStruct auto-generated)
- **Configuration**: `MapperSpringConfig` (Java/Kotlin), `ModelMapperConfig` (Groovy)

## Package Structure

- **Configuration**: `src/main/{language}/config/mapper/`
- **Mappers**: `src/main/{language}/{domain}/{entity}/adapter/in/rest/`
- **Tests**: `src/test/{language}/{domain}/{entity}/adapter/in/rest/`

## Mapper Definition Patterns

**Java**:
```java
@Mapper(config = MapperSpringConfig.class)
public interface EntityMapper extends Converter<DomainEntity, GeneratedDto> {
  @Override
  GeneratedDto convert(@Nullable DomainEntity source);
}
```

**Kotlin**:
```kt
@Mapper(config = MapperSpringConfig::class)
interface EntityMapper : Converter<DomainEntity, GeneratedDto> {
  override fun convert(source: DomainEntity): GeneratedDto
}
```

**Groovy**:
```groovy
@Component
@CompileStatic
class EntityMapper {
  private final ModelMapper modelMapper

  EntityMapper(ModelMapper modelMapper) {
    this.modelMapper = modelMapper
  }

  GeneratedDto convert(DomainEntity source) {
    if (!source) return null
    def target = modelMapper.map(source, GeneratedDto)
    // Add custom logic for enums or computed fields
    target
  }
}
```

## Key Differences

| Aspect          | Java/Kotlin (MapStruct)                   | Groovy (ModelMapper)                    |
|-----------------|-------------------------------------------|-----------------------------------------|
| Code generation | Auto-generated at compile time            | Manual implementation                   |
| Custom logic    | `@Mapping` annotations or default methods | Explicit code after `modelMapper.map()` |
| Null handling   | Automatic in generated code               | Must add explicit checks                |

## Usage in Controllers

```java
@RestController
@RequiredArgsConstructor
public class ResourceController {
  private final ResourceMapper mapper;
  
  public ResponseEntity<ResourceResponse> findById(Long id) {
    return ok(new ResourceResponse()
        .data(mapper.convert(domainObject))
        .status(OK.value()));
  }
}
```

## Testing

- **MapStruct**: Import generated `XxxMapperImpl` in `@WebMvcTest` imports
- **ModelMapper**: Test manual `convert` method handles nulls and custom logic
