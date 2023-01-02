package exporter;

import helper.MQTTTopicSubscriber;
import helper.MQTTWebsocketTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.UUID;

public class MQTTExporter extends Exporter {
    private static final Logger LOGGER = LogManager.getLogger(MQTTExporter.class);
    public final static String prefix = "update";
    private String topicTelemetryUploadAPI = prefix + "/telemetry";
    private String topicAttribytesAPI = prefix + "/attributes";
    public MQTTExporter(Exporter exporter) {
        super(exporter);
    }

    public void publishAttributes(String token, JSONObject json) {

        LOGGER.info("publish atttributes - token: " + token + ", json:" + json.toString());
        json.put("command","pushattributes");
        try {
            String publishMsg = json.toString();
            MQTTWebsocketTopicPublisher publisher = new MQTTWebsocketTopicPublisher();
            String clientID = "MQTTWebsocket_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(topicAttribytesAPI, publishMsg);
                publisher.closeConnection();
            }
        } catch (Exception e) {
            LOGGER.error("failed to publish attributes" + e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void publishPowerMetric(JSONObject json) {
        LOGGER.info("publish telemetry - json:" + json.toString());
        json.put("command","pushtelemetry");
        try {
            MQTTWebsocketTopicPublisher publisher = new MQTTWebsocketTopicPublisher();
            String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(topicTelemetryUploadAPI, json.toString());
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("failed to publish telemetry" + e.toString());
            e.printStackTrace();
        }
    }
}
