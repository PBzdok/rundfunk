FROM gradle:jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM eclipse-temurin:17.0.4_8-jre-jammy

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/rundfunk.jar
ENTRYPOINT ["java", "-jar", "-Xms128m","-Xmx256m", "/app/rundfunk.jar"]
