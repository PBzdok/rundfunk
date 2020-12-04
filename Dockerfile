FROM gradle:jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM adoptopenjdk:11.0.9_11-jre-openj9-0.23.0

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/unitbot.jar
ENTRYPOINT ["java", "-jar", "-Xms128m","-Xmx512m", "/app/unitbot.jar"]
