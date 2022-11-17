package exporter;

import config.Configuration;
import helper.MQTTTopicPublisher;

import java.time.LocalDateTime;

public class ThingsBoardExporter extends Exporter {
    public ThingsBoardExporter(Exporter exporter) {
        this.setName(exporter.getName());
    }


    public void publishPowerMetric(String name, String type, String token, LocalDateTime localDateTime, double power) {

        try {
            String publishTopic = Configuration.getExporters().get(0).getTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + power + "\"}";

            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            publisher.createConnection(Configuration.getExporters().get(0).gethost(), token,"");
            publisher.publishMessage(publishTopic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
