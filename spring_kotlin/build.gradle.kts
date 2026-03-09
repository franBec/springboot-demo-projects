import java.util.Properties

plugins {
  kotlin("jvm") version "2.2.21"
  kotlin("plugin.spring") version "2.2.21"
  id("org.springframework.boot") version "4.0.3"
  id("io.spring.dependency-management") version "1.1.7"
  id("com.diffplug.spotless") version "8.1.0"
  kotlin("kapt") version "2.3.10"
  id("org.openapi.generator") version "7.20.0"
  jacoco
  kotlin("plugin.jpa") version "2.2.21"
}

group = "dev.pollito"

version = "0.0.1-SNAPSHOT"

description = "Demo project for Spring Boot with Kotlin"

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

configurations { compileOnly { extendsFrom(configurations.annotationProcessor.get()) } }

val hibernateTools: Configuration by configurations.creating

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

  val swaggerCoreVersion = "2.2.44"
  implementation("io.swagger.core.v3:swagger-annotations:$swaggerCoreVersion")
  implementation("io.swagger.core.v3:swagger-models:$swaggerCoreVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  testImplementation("com.ninja-squad:springmockk:5.0.1")
  testImplementation("io.mockk:mockk:1.14.7")

  implementation("io.micrometer:micrometer-registry-prometheus:1.17.0-M2")

  val hibernateVersion = "7.0.2.Final"
  val h2Version = "2.4.240"
  hibernateTools("com.h2database:h2:$h2Version")
  hibernateTools("org.hibernate.tool:hibernate-tools-ant:$hibernateVersion")
  hibernateTools("org.hibernate.orm:hibernate-core:$hibernateVersion")

  developmentOnly("com.h2database:h2:$h2Version")
  testRuntimeOnly("com.h2database:h2:$h2Version")
  developmentOnly("org.springframework.boot:spring-boot-h2console")
  runtimeOnly("org.postgresql:postgresql")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
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

tasks.named("check") { dependsOn(tasks.jacocoTestCoverageVerification) }

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
val openApiGeneratedSourcesDir = "${layout.buildDirectory.get().asFile}/generated/sources/openapi"
val hibernateGeneratedSourcesDir = layout.buildDirectory.dir("generated/sources/hibernate")

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

tasks.register("generateEntities") {
  group = "build"
  description = "Reverse engineers resources/sakila.sql into JPA Entities (Kotlin)"

  val sqlFile = file("src/main/resources/sakila-schema.sql")
  val revengFile = file("src/main/resources/hibernate.reveng.xml")
  val basePropsFile = file("src/main/resources/hibernate-tools.properties")
  val templateDir = file("src/main/resources/templates/hibernate")

  inputs.file(sqlFile)
  inputs.file(revengFile)
  inputs.file(basePropsFile).optional()
  inputs.dir(templateDir).optional()
  outputs.dir(hibernateGeneratedSourcesDir)

  doLast {
    val tempPropsFile = layout.buildDirectory.file("tmp/hibernate-tools.properties").get().asFile
    tempPropsFile.parentFile.mkdirs()

    val props = Properties()
    if (basePropsFile.exists()) {
      basePropsFile.inputStream().use { stream -> props.load(stream) }
    }

    val sakilaSqlPath = sqlFile.absolutePath.replace("\\", "/")
    props.setProperty(
        "hibernate.connection.url",
        "jdbc:h2:mem:sakila;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM '$sakilaSqlPath'",
    )

    tempPropsFile.outputStream().use { stream -> props.store(stream, null) }

    val destDir = hibernateGeneratedSourcesDir.get().asFile
    destDir.mkdirs()

    ant.withGroovyBuilder {
      "taskdef"(
          "name" to "hibernatetool",
          "classname" to "org.hibernate.tool.ant.HibernateToolTask",
          "classpath" to hibernateTools.asPath,
      )

      "hibernatetool"(
          "destdir" to destDir,
          "templatepath" to templateDir,
      ) {
        "jdbcconfiguration"(
            "propertyfile" to tempPropsFile,
            "revengfile" to revengFile,
            "packagename" to "${project.group}.${project.name}.generated.entity",
            "detectmanytomany" to true,
            "detectoptimisticlock" to true,
        )
        "hbm2java"("jdk5" to true, "ejb3" to true)
      }
    }

    val entityDir =
        File(destDir, "${project.group}.${project.name}.generated.entity".replace('.', '/'))
    entityDir
        .listFiles()
        ?.filter { it.extension == "java" }
        ?.forEach { javaFile ->
          val ktFile = File(javaFile.parentFile, "${javaFile.nameWithoutExtension}.kt")
          javaFile.renameTo(ktFile)
          println("Renamed: ${javaFile.name} -> ${ktFile.name}")
        }
  }
}

kotlin.sourceSets["main"].kotlin {
  srcDir("$openApiGeneratedSourcesDir/src/main/kotlin")
  srcDir(hibernateGeneratedSourcesDir)
}

tasks.named("compileKotlin") {
  dependsOn("generateOpenApi")
  dependsOn("generateEntities")
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask> {
  dependsOn("generateOpenApi")
  dependsOn("generateEntities")
}

tasks.named("clean") { doFirst { delete(openApiGeneratedSourcesDir) } }
