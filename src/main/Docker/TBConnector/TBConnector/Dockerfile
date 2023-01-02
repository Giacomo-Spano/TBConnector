FROM eclipse-temurin:18
RUN apt-get update && apt-get install -y systemd
RUN mkdir /app
RUN mkdir /app/TBConnector
COPY ./*.jar /app/TBConnector/
COPY ./*.properties /app/TBConnector/
RUN mkdir app/TBConnector/config
RUN mkdir app/TBConnector/logs
COPY config/configuration.yaml /app/TBConnector/config/
COPY config/schedule.yaml /app/TBConnector/config/
WORKDIR /app/TBConnector
EXPOSE 8081
EXPOSE 8090
RUN apt-get update && \
    apt-get install -y nano
CMD ["java", "-jar", "/app/TBConnector/TBConnector.jar"]