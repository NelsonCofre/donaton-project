FROM eclipse-temurin:21-jdk-alpine AS build
RUN apk add --no-cache bash unzip curl
WORKDIR /app

ENV GRADLE_VERSION=8.14.4
RUN curl -fsSL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o /tmp/gradle.zip \
	&& unzip -q /tmp/gradle.zip -d /opt \
	&& rm /tmp/gradle.zip
ENV PATH="/opt/gradle-${GRADLE_VERSION}/bin:${PATH}"

COPY settings.gradle build.gradle ./
COPY src ./src

RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
