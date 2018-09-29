FROM openjdk:10
COPY . /src
WORKDIR /src
RUN ./gradlew build
ENTRYPOINT ./gradlew bootRun
