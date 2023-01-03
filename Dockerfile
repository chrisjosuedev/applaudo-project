FROM openjdk:17-oracle
EXPOSE 9090
WORKDIR /app
COPY ../target/applaudo-final-project-1.0.0-SNAPSHOT.jar .
ENTRYPOINT [ "java", "-jar", "applaudo-final-project-1.0.0-SNAPSHOT.jar" ]