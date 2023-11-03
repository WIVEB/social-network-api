# Use the official OpenJDK image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

RUN echo $(ls)

# Copy the Ktor application JAR file into the container
COPY socialnetwork-${VERSION}.jar /app/socialnetwork-${VERSION}.jar

# Define the command to run your Ktor application
CMD ["java", "-jar", "socialnetwork-${VERSION}.jar"]
