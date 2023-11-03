FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

# Get the version from Gradle task and store it in a variable
RUN VERSION=$(gradle -q getVersion)

FROM openjdk:11
EXPOSE 8080
RUN mkdir /app

# Use the version captured from previous stage
ARG VERSION
ENV APP_VERSION=${VERSION}

COPY --from=build /home/gradle/src/build/libs/socialnetwork-${VERSION}.jar /app/socialnetwork.jar
ENTRYPOINT ["java","-jar","/app/socialnetwork.jar"]