FROM openjdk:21-jdk-slim
ENV APP_FILE=api-service-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/app
EXPOSE 8000
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT [ "sh", "-c" ]
CMD [ "exec java -jar $APP_FILE" ]