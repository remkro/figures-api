FROM openjdk:17-alpine
ADD target/figures-api-docker.jar figures-api-docker.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar","figures-api-docker.jar"]
EXPOSE 8081