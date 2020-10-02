FROM gradle:jdk14 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:14-jre-slim

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/unitbot.jar
ENTRYPOINT ["java", "-jar","-Xmx128m", "/app/unitbot.jar"]
