plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  id("org.springframework.boot") version "4.0.1"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.diffplug.spotless") version "8.1.0"
  kotlin("kapt") version "2.3.10"
  id("org.openapi.generator") version "7.20.0"
  jacoco
  id("info.solidsoft.pitest") version "1.19.0-rc.3"
}

group = "dev.pollito"

version = "0.0.1-SNAPSHOT"

description = "Demo project for Spring Boot with Kotlin"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

configurations { compileOnly { extendsFrom(configurations.annotationProcessor.get()) } }

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("tools.jackson.module:jackson-module-kotlin")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  val mapstructVersion = "1.6.3"
  val mapstructSpringExtensionsVersion = "2.0.0"
  implementation("org.mapstruct:mapstruct:$mapstructVersion")
  kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
  implementation(
      "org.mapstruct.extensions.spring:mapstruct-spring-annotations:$mapstructSpringExtensionsVersion"
  )
  kapt(
      "org.mapstruct.extensions.spring:mapstruct-spring-extensions:$mapstructSpringExtensionsVersion"
  )

  implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
  implementation("org.aspectj:aspectjtools:1.9.25.1")
  implementation("org.springframework.boot:spring-boot-starter-opentelemetry")

  val swaggerCoreVersion = "2.2.41"
  implementation("io.swagger.core.v3:swagger-annotations:$swaggerCoreVersion")
  implementation("io.swagger.core.v3:swagger-models:$swaggerCoreVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  testImplementation("com.ninja-squad:springmockk:5.0.1")
  testImplementation("io.mockk:mockk:1.14.7")

  implementation("io.micrometer:micrometer-registry-prometheus:1.17.0-M2")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
  finalizedBy(tasks.jacocoTestReport)
}

jacoco { toolVersion = "0.8.14" }

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required.set(true)
    html.required.set(true)
  }

  classDirectories.setFrom(
      files(
          classDirectories.files.map {
            fileTree(it) {
              exclude(
                  // OpenAPI generated code
                  "**/generated/**",
                  "**/openapitools/**",

                  // Application entry point
                  "**/*Application*",

                  // Domain models (POJOs)
                  "**/domain/model/**",

                  // MapStruct
                  "**/config/mapper/**",
                  "**/*MapperImpl*",
              )
            }
          }
      )
  )
}

tasks.jacocoTestCoverageVerification {
  dependsOn(tasks.jacocoTestReport)

  violationRules {
    rule {
      limit {
        counter = "LINE"
        minimum = "0.8".toBigDecimal()
      }
      limit {
        counter = "BRANCH"
        minimum = "0.5".toBigDecimal()
      }
    }
  }

  classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
}

tasks.named("check") { dependsOn(tasks.jacocoTestCoverageVerification, tasks.pitest) }

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlin {
    target("src/**/*.kt")
    targetExclude("build/**/*.kt")
    ktfmt()
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}

tasks.named("build") {
  dependsOn("spotlessKotlinApply")
  dependsOn("spotlessKotlinGradleApply")
}

val openApiSpecPath = "$projectDir/src/main/resources/openapi.yaml"
val openApiGeneratedSourcesDir = "${layout.buildDirectory.get().asFile}/generated/source/openapi"

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenApi") {
  generatorName.set("kotlin-spring")
  generateApiTests.set(false)
  generateApiDocumentation.set(false)
  generateModelTests.set(false)
  generateModelDocumentation.set(false)

  inputSpec.set(openApiSpecPath)
  outputDir.set(openApiGeneratedSourcesDir)

  val basePackage = "${project.group}.${project.name}.generated"
  apiPackage.set("$basePackage.api")
  modelPackage.set("$basePackage.model")

  configOptions.set(
      mapOf(
          "gradleBuildFile" to "false",
          "interfaceOnly" to "true",
          "modelMutable" to "true",
          "requestMappingMode" to "api_interface",
          "skipDefaultInterface" to "true",
          "useJakartaEe" to "true",
          "useSpringBoot3" to "true",
          "useTags" to "true",
      )
  )
}

kotlin.sourceSets["main"].kotlin.srcDir("$openApiGeneratedSourcesDir/src/main/kotlin")

tasks.named("compileKotlin") { dependsOn("generateOpenApi") }

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask> {
  dependsOn("generateOpenApi")
}

tasks.named("clean") { doFirst { delete(openApiGeneratedSourcesDir) } }

pitest {
  junit5PluginVersion.set("1.2.3")
  threads.set(Runtime.getRuntime().availableProcessors())
  outputFormats.set(setOf("HTML"))
  timestampedReports.set(false)
  jvmArgs.set(listOf("-XX:+EnableDynamicAgentLoading", "-Xshare:off"))
  mainProcessJvmArgs.set(listOf("-XX:+EnableDynamicAgentLoading", "-Xshare:off"))

  avoidCallsTo.set(
      setOf(
          "kotlin.jvm.internal",
          "kotlin.ResultKt",
          "org.slf4j",
          "io.github.oshai.kotlinlogging",
          "ch.qos.logback",
      )
  )

  val basePackage = "${project.group}.${project.name}"
  targetClasses.set(
      setOf(
          "$basePackage.config.advice.*",
          "$basePackage.config.log.*",
          "$basePackage.sakila.*.adapter.*",
          "$basePackage.sakila.*.domain.port.*",
      )
  )
  targetTests.set(setOf("$basePackage.*"))
  excludedClasses.set(
      setOf(
          "$basePackage.generated.*",
          "**.*MapperImpl*",
      )
  )

  mutationThreshold = 70
  coverageThreshold = 80
}
