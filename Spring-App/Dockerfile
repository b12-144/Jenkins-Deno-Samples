FROM openjdk:11-jre-slim
EXPOSE 8080
ADD /build/libs/demo-0.0.1-SNAPSHOT.jar dockerdemo.jar
ENTRYPOINT ["java", "-jar", "dockerdemo.jar"]
