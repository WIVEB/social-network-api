FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

# Get the version from Gradle and store it as an environment variable
FROM openjdk:11
EXPOSE 8080
RUN mkdir /app

# Use the environment variable captured from previous stage
ARG APP_VERSION
ENV VERSION=${APP_VERSION}

COPY --from=build /home/gradle/src/build/libs/socialnetwork-*.jar socialnetwork.jar /app/
ENTRYPOINT ["java","-jar","/app/socialnetwork.jar"]
