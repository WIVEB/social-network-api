FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/socialnetwork-0.0.1.jar /app/socialnetwork.jar
ENTRYPOINT ["java","-jar","/app/socialnetwork.jar"]