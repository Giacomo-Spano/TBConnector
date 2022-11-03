FROM eclipse-temurin:18
RUN mkdir /app
RUN mkdir /app/TBConnector
COPY /TBConnector/*.jar /app/TBConnector
RUN mkdir app/TBConnector/config
COPY /config/configuration.yaml /app/TBConnector/config
WORKDIR /app/TBConnector 
CMD ["java", "-jar", "/app/TBConnector/TBConnector.jar"]