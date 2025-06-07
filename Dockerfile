FROM openjdk:24-ea-jdk-oraclelinux8

WORKDIR /app
COPY build/libs/springBootConditional-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
