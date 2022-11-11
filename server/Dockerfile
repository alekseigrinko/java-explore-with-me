FROM amazoncorretto:11
ARG JAR_FILE=target/ewm-main-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ewm-main-service.jar
ENTRYPOINT ["java","-jar","ewm-main-service.jar"]
