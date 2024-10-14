FROM openjdk:17-oracle
LABEL authors="phhs"
COPY target/*.jar cabonerf.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cabonerf.jar"]