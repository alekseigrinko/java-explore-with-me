FROM amazoncorretto:11
ARG JAR_FILE=target/explore-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} explore-server.jar
ENTRYPOINT ["java","-jar","explore-server.jar"]
