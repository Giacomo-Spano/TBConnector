FROM eclipse-temurin:18
RUN apt-get update && apt-get install -y systemd
RUN mkdir /app
RUN mkdir /app/TBConnector
COPY ./*.jar /app/TBConnector/
RUN mkdir app/TBConnector/config
COPY ./config/configuration.yaml /app/TBConnector/config/
WORKDIR /app/TBConnector
EXPOSE 8080
RUN apt-get update && \
    apt-get install -y nano
CMD ["java", "-jar", "/app/TBConnector/TBConnector.jar"]