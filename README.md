# Spring Boot Demo Projects Monorepo

This repository serves as a demo project for "Pollito's Opinion on Spring Boot Development," a guide designed for developers looking to quickly jump into a codebase and ship code.

## Project Structure

This is a Gradle monorepo containing three independent Spring Boot projects, each implemented in a different JVM language:

- **`spring_java`**: A Spring Boot application written in Java.
- **`spring_kotlin`**: A Spring Boot application written in Kotlin.
- **`spring_groovy`**: A Spring Boot application written in Groovy.

## Purpose

The primary goal of this project is to provide practical, hands-on examples that complement the "Pollito's Opinion on Spring Boot Development" documentation (available at [https://springboot.pollito.tech/](https://springboot.pollito.tech/)). Developers can use this monorepo to follow along with the guide, experiment with the different language implementations, and understand how to set up and manage multiple Spring Boot services within a single Gradle build.

## Getting Started

To get started with this project, clone the repository and refer to the individual `HELP.md` files within each subproject for specific instructions on how to run and interact with each Spring Boot application.

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Gradle (included via Gradle Wrapper)

### Building the Project

You can build all projects in the monorepo using the Gradle Wrapper:

```bash
./gradlew build
```

This will compile, test, and package all three Spring Boot applications.

## Contribution

This project is intended as a companion to the "Pollito's Opinion on Spring Boot Development" guide. While direct contributions to this demo repository are not actively sought, feedback on the guide itself is always welcome.
