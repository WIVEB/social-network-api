# Use the openjdk image as the base image
FROM openjdk:11

# Create a directory in the container
RUN mkdir /app

# Copy the JAR file
COPY socialnetwork-*.jar /app/socialnetwork.jar

# Expose the port
EXPOSE 8080

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "/app/socialnetwork.jar"]