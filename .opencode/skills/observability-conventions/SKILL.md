---
name: observability-conventions
description: Spring Boot observability patterns using OpenTelemetry, Prometheus, Loki, Tempo, and Grafana for metrics, logs, and traces
license: MIT
compatibility: opencode
metadata:
  domain: observability
  stack: spring-boot
  ecosystem: opentelemetry
---

## What I do

Guide implementation of full-stack observability in Spring Boot modules following established conventions:

- Configure OpenTelemetry instrumentation and trace propagation
- Set up standardized logging with trace context and sensitive data masking
- Expose Prometheus metrics via Actuator endpoints
- Integrate with Loki, Tempo, and Grafana for the complete observability stack

## When to use me

Use this skill when:

- Adding observability to a new Spring Boot module
- Configuring logging with trace correlation
- Setting up metrics endpoints for Prometheus
- Implementing request/response logging filters
- Troubleshooting trace propagation issues
- Configuring the local observability stack

## Logging Conventions

### Log Levels

| Level | Usage                                                   |
|-------|---------------------------------------------------------|
| ERROR | Critical errors affecting application functionality     |
| WARN  | Potential issues that don't stop execution              |
| INFO  | Important operational information (requests, responses) |

### Log Format

All modules must use this standardized format with trace context:

```
%d{yyyy-MM-dd} %d{HH:mm:ss.SSS} trace_id=%X{trace_id} span_id=%X{span_id} trace_flags=%X{trace_flags} %-5level %thread --- %logger{36} %msg%n
```

### Key Components

- **TraceIdFilter**: Manages MDC context for trace correlation
- **MaskingPatternLayout**: Implements sensitive data masking
- **LogFilter**: Logs request/response details (method, URI, headers, status)
- **LogAspect**: Logs method arguments and return values via AOP

### Request/Response Flow

1. **TraceIdFilter**: Sets trace_id in MDC
2. **LogFilter**: Logs request details (method, URI, headers)
3. **LogAspect**: Logs method args and responses
4. **LogFilter**: Logs response status
5. **TraceIdFilter**: Clears MDC

## Tracing Conventions

### OpenTelemetry Configuration

```yaml
management:
  otlp:
    tracing:
      endpoint: http://tempo:4318/v1/traces
  tracing:
    sampling:
      probability: 1.0
```

All modules must integrate `spring-boot-starter-opentelemetry`.

## Metrics Conventions

### Actuator Configuration

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
    prometheus:
      enabled: true
```

### Metric Categories

- **HTTP Server Metrics**: Request latency, status codes, throughput
- **Business Metrics**: Domain-specific metrics
- **Application Metrics**: JVM metrics, Spring Boot metrics

Metrics exposed at `/actuator/prometheus`.

## Development Profile

Disable metrics export in development:

```yaml
management:
  otlp:
    metrics:
      export:
        enabled: false
```

## Observability Stack

| Service    | Port             | Purpose                     |
|------------|------------------|-----------------------------|
| Grafana    | 3000             | Visualization (admin/admin) |
| Prometheus | 9090             | Metrics collection          |
| Loki       | 3100             | Log aggregation             |
| Tempo      | 3200, 4317, 4318 | Distributed tracing         |
| Promtail   | 9080             | Log shipping                |

All services use the `monitoring` bridge network.

## Configuration Files

### Each Module

- `application.yaml` - Main observability config
- `application-dev.yaml` - Dev-specific settings
- `logback-spring.xml` - Logging with masking

### Stack

- `docker-compose.yml` - Service orchestration
- `observability/prometheus.yml` - Scraping config
- `observability/tempo.yml` - Tracing config
- `observability/loki-config.yml` - Log config
- `observability/promtail-config.yml` - Log shipping
- `observability/grafana/datasources/datasources.yml` - Data sources
- `observability/grafana/dashboards/dashboards.yml` - Dashboards

## Language-Specific Logging

| Language | Implementation        |
|----------|-----------------------|
| Java     | `@Slf4j` with Lombok  |
| Kotlin   | kotlin-logging        |
| Groovy   | `@Slf4j` Groovy-style |

## Best Practices

1. Use appropriate log levels to avoid noise
2. All errors must be traceable with correlation IDs
3. Never log sensitive data without masking
4. Include trace context in all log statements