FROM openjdk:11.0.3-jre-stretch
ARG JAR_FILE
ARG CONSUMER_KEY
ARG CONSUMER_SECRET
ARG ACCESS_TOKEN
ARG ACCESS_TOKEN_SECRET
ARG TELEGRAM_API
COPY ${JAR_FILE} app.jar
CMD java -jar app.jar ${CONSUMER_KEY} ${CONSUMER_SECRET} ${ACCESS_TOKEN} ${ACCESS_TOKEN_SECRET} ${TELEGRAM_API}
