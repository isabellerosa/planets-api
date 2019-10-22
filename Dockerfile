FROM maven:3.6.2-jdk-8-slim

COPY . /app
WORKDIR /app

RUN mvn install -DskiptTests && mv target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-jar","app.jar"]