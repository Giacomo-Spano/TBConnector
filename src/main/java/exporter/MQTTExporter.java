package exporter;

import helper.MQTTTopicPublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.UUID;

public class MQTTExporter extends Exporter {
    private static final Logger LOGGER = LogManager.getLogger(MQTTExporter.class);
    //public final static String prefix = "giacomo.spano@libero.it/update";
    private String topicTelemetryUploadAPI;
    private String topicAttribytesAPI;
    public MQTTExporter(Exporter exporter) {
        super(exporter);
        topicTelemetryUploadAPI = getPrefix() + "/telemetry";
        topicAttribytesAPI = getPrefix() + "/attributes";
        LOGGER.info("MQTTExporter - topicTelemetryUploadAPI: " + topicTelemetryUploadAPI + ", topicAttribytesAPI:" + topicAttribytesAPI);
    }

    public void publishAttributes(String token, JSONObject json) {

        LOGGER.info("publish atttributes - token: " + token + ", json:" + json.toString());
        JSONObject jsonPacket = new JSONObject();
        jsonPacket.put("data", json);
        jsonPacket.put("command","pushattributes");
        try {
            String publishMsg = jsonPacket.toString();
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
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
        LOGGER.info("attributes published");
    }

    public void publishTelemetry(JSONObject json, String deviceId, String type) {
        LOGGER.info("publish telemetry - json:" + json.toString());

        JSONObject jsonPacket = new JSONObject();
        jsonPacket.put("data", json);
        jsonPacket.put("command","pushtelemetry");
        jsonPacket.put("deviceid", deviceId);
        jsonPacket.put("type", type);
        try {
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "pubTelemetry_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(topicTelemetryUploadAPI, jsonPacket.toString());
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("failed to publish telemetry" + e.toString());
            e.printStackTrace();
        }
        LOGGER.info("telemetry published");
    }
}
