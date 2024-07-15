# First stage: Build the application
FROM gradle:7.6.0-jdk17-alpine AS build

# Set the working directory
WORKDIR /home/gradle/project

# Copy the project files
COPY . .

# Build the application
RUN gradle build --no-daemon

# Second stage: Run the application
FROM openjdk:22-jdk-alpine

# Set the working directory
WORKDIR /opt/app

# Copy the jar file from the build stage
COPY --from=build /home/gradle/project/build/libs/*-0.0.1-SNAPSHOT.jar /opt/app/app.jar

# Expose the application port
EXPOSE 80

# Run the application
CMD ["java", "-showversion", "-jar", "/opt/app/app.jar"]