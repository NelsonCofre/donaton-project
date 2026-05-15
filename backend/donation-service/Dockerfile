# Etapa 1 — build: compila el JAR con Gradle Wrapper (JDK 21, Temurin Alpine).
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

RUN apk add --no-cache bash unzip

COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle gradle

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon -q

COPY src src

RUN ./gradlew bootJar --no-daemon -x test

# Etapa 2 — runtime: solo JRE 21 y el artefacto ejecutable.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown nobody:nobody app.jar

USER nobody

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
