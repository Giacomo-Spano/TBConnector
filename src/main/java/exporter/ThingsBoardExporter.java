package exporter;

import helper.MQTTTopicPublisher;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.UUID;

public class ThingsBoardExporter extends Exporter {

    private String topicTelemetryUploadAPI = "v1/devices/me/telemetry";
    private String topicAttribytesAPI = "v1/devices/me/attributes";


    public ThingsBoardExporter(Exporter exporter) {
        super(exporter);
    }

    public void publishAttributes(String token, JSONObject json) {

        try {
            String publishMsg = json.toString();
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, token, "")) {
                publisher.publishMessage(topicAttribytesAPI, publishMsg);
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void publishTelemetry(String name, String type, String token, LocalDateTime localDateTime, double power) {

        try {
            String publishTopic = getTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + power + "\"}";

            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, token,"")) {
                publisher.publishMessage(publishTopic, publishMsg);
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void publishTelemetry(JSONObject json) {

        String token = json.getString("deviceid");
        try {
            String publishTopic = getTopic();//"v1/devices/me/telemetry";

            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, token,"")) {
                publisher.publishMessage(publishTopic, json.toString());
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
