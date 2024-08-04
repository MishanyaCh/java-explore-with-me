FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar /main-service-app.jar
ENTRYPOINT ["java", "-jar", "/main-service-app.jar"]