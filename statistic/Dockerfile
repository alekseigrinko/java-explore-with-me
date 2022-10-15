FROM amazoncorretto:11
ARG JAR_FILE=target/explore-statistic-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} explore-statistic.jar
ENTRYPOINT ["java","-jar","explore-statistic.jar"]
