FROM amazoncorretto:11
ARG JAR_FILE=target/ewm-statistic-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ewm-statistic-service.jar
ENTRYPOINT ["java","-jar","ewm-statistic-service.jar"]
