FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

RUN apk add --no-cache bash unzip curl ca-certificates

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle

# CRLF (Windows/OneDrive) y carpetas compartidas de VM: limpiar y ejecutar con bash.
RUN sed -i 's/\r$//' gradlew \
	&& chmod +x gradlew \
	&& bash ./gradlew dependencies --no-daemon --no-watch-fs

COPY src src

RUN bash ./gradlew bootJar --no-daemon --no-watch-fs -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown nobody:nobody app.jar

USER nobody

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
