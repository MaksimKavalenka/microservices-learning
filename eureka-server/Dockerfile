FROM amazoncorretto:17.0.8-alpine
COPY /build/libs/eureka-server-0.1.0.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
