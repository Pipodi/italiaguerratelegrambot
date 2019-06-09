FROM openjdk:11.0.3-jre-stretch
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
CMD java -jar app.jar
