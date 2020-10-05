FROM openjdk:8u171-alpine

ADD ./target/backend-0.0.1-SNAPSHOT.jar /run/

EXPOSE 8080

ENTRYPOINT ['java -jar /run/backend-0.0.1-SNAPSHOT.jar']
