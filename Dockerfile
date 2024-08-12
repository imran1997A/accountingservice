FROM openjdk:17-jdk
WORKDIR /opt
COPY '/target/*.jar' '/opt/app.jar'
EXPOSE 8080
CMD ["java", "-jar", "/opt/app.jar"]