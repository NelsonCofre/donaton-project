# Etapa 1 — build: compila el JAR con Gradle Wrapper (JDK 21, Temurin Alpine).
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

RUN apk add --no-cache bash unzip curl ca-certificates

COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle gradle

# CRLF (Windows/OneDrive) y carpetas compartidas de VM: limpiar y ejecutar con bash.
RUN sed -i 's/\r$//' gradlew \
	&& chmod +x gradlew \
	&& bash ./gradlew dependencies --no-daemon --no-watch-fs

COPY src src

RUN bash ./gradlew bootJar --no-daemon --no-watch-fs -x test

# Etapa 2 — runtime: solo JRE 21 y el artefacto ejecutable.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown nobody:nobody app.jar

USER nobody

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
