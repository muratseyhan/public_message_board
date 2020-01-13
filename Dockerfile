FROM gradle:6.0.1-jdk13 AS build
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN rm -rf build
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk13:jre-13.0.1_9-alpine
ENV version 0.0.1-SNAPSHOT
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/app/build/libs/message_board-$version.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar", "--debug"]
