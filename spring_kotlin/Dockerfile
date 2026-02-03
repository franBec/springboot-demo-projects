FROM gradle:jdk21-alpine AS build
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle spring_kotlin/settings-docker.gradle ./settings.gradle
COPY --chown=gradle:gradle gradle ./gradle
COPY --chown=gradle:gradle gradlew ./
COPY --chown=gradle:gradle spring_kotlin ./spring_kotlin

RUN gradle :spring_kotlin:bootJar -x test -x spotlessApply -x spotlessCheck --no-daemon

FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=build /home/gradle/src/spring_kotlin/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]