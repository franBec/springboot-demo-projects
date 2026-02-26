---
name: coolify-deployment
description: Deploy Spring Boot modules to VPS using Coolify, Docker Compose, and GitHub Actions CI/CD pipeline with observability stack
license: MIT
compatibility: opencode
metadata:
  stack: spring-boot, docker, coolify, github-actions
  category: devops
---

## What I do

- Guide deployment setup for Spring Boot apps on Coolify-managed VPS
- Configure Docker Compose multi-service deployments
- Set up GitHub Actions CI/CD with test gating
- Integrate observability stack (Prometheus, Loki, Tempo, Grafana)

## When to use me

Use this skill when:

- Setting up new deployments to Coolify
- Troubleshooting CI/CD pipeline failures
- Adding observability to Spring Boot services
- Configuring Docker Compose for production

## Deployment Flow

1. Push to `main` → GitHub Actions triggered
2. `build-and-test` job compiles and runs tests
3. `deploy` job calls Coolify webhook **only if CI passes**
4. Coolify builds Docker containers and deploys

## Required Files

| File                              | Purpose                                |
|-----------------------------------|----------------------------------------|
| `docker-compose.yml`              | Defines all services with healthchecks |
| `<module>/Dockerfile`             | Multi-stage build per module           |
| `<module>/settings-docker.gradle` | Isolated Gradle settings for Docker    |
| `.github/workflows/ci-cd.yml`     | CI/CD pipeline                         |

**docker-compose.yml**:

- Each module as separate service
- Builds from module's Dockerfile
- Exposes port 8080 internally
- Activates `prod` profile
- Includes healthcheck via Spring Boot Actuator
- Auto-restart on crash

**Module Dockerfiles**:

- Multi-stage build (build + runtime)
- `settings-docker.gradle` for minimal build
- Slim JRE Alpine runtime
- `curl` installed for healthcheck

**Gradle Wrapper Exception**:

`gradle/wrapper/gradle-wrapper.jar` must NOT be in `.gitignore` (Docker builds need it)

**GitHub Actions** (`.github/workflows/ci-cd.yml`):

- `build-and-test`: Runs on every push/PR
- `deploy`: Runs on `main` after CI succeeds

## Coolify Configuration

1. Create Resource: "Private Repository (with GitHub App)"
2. Build Pack: Docker Compose
3. Docker Compose Location: `/docker-compose.yml`
4. Domains: Configure per service (e.g., `java-project.domain.com:8080`)
5. **Disable Auto Deploy**: Critical - prevents bypassing CI

**Required Secrets**:

- `COOLIFY_API_TOKEN`: Coolify API token
- `COOLIFY_DEPLOY_UUID`: Deploy webhook UUID

## Observability Stack

**Three Pillars**:

| Data Type | Purpose              | Backend    |
|-----------|----------------------|------------|
| Logs      | Event records        | Loki       |
| Metrics   | CPU, memory, latency | Prometheus |
| Traces    | Request journey      | Tempo      |

**Grafana**: Unified visualization layer

**Spring Boot Apps**:

- `micrometer-registry-prometheus` dependency
- Metrics at `/actuator/prometheus`
- Traces via OTLP to Tempo

**Services**:

- **Prometheus**: Metrics collection (60s interval)
- **Loki**: Log storage (15-day retention)
- **Promtail**: Docker log collector
- **Tempo**: Distributed tracing (ports 4317/4318)
- **Grafana**: Dashboards (default: admin/admin)

**Grafana Access**:

- Change default password immediately
- Pre-configured JVM and Spring Boot dashboards
- Traces-to-logs integration enabled