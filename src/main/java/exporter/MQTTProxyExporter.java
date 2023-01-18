package exporter;

import config.Configuration;
import device.Device;
import helper.MQTTTopicPublisher;
import helper.MQTTTopicSubPub;
import helper.MQTTTopicSubscriber;
import importer.Importer;
import importer.ImporterInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MQTTProxyExporter extends Exporter {
    private static final Logger LOGGER = LogManager.getLogger(MQTTProxyExporter.class);
    private String topicUpdate;

    private MQTTTopicSubPub ts;

    public MQTTProxyExporter(Exporter exporter) {
        super(exporter);
        topicUpdate = getPrefix() + "/update";
        LOGGER.info("MQTTExporter - topicTelemetryUploadAPI: " + topicUpdate);
    }

    public void publishAttributes(String token, JSONObject json) {

        LOGGER.info("publish atttributes - token: " + token + ", json:" + json.toString());
        JSONObject jsonPacket = new JSONObject();
        jsonPacket.put("data", json);
        jsonPacket.put("command","pushattributes");
        try {
            String publishMsg = jsonPacket.toString();
            //MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "MQTTWebsocket_" + UUID.randomUUID().toString().substring(0,8);
            /*if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(topicUpdate, publishMsg);
                publisher.closeConnection();
            }*/
            ts.publishMessage(topicUpdate, publishMsg);
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
            //String clientID = "pubTelemetry_" + UUID.randomUUID().toString().substring(0,8);
            /*if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(topicUpdate, jsonPacket.toString());
                publisher.closeConnection();
            }*/
            ts.publishMessage(topicUpdate, jsonPacket.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("failed to publish telemetry" + e.toString());
            e.printStackTrace();
        }
        LOGGER.info("telemetry published");
    }

    public void publishBackCommand(String deviceid, String command, JSONObject param) {
        LOGGER.info("publish telemetry - json:" + param.toString());

        JSONObject jsonPacket = new JSONObject();
        jsonPacket.put("param", param);
        jsonPacket.put("command", command);
        jsonPacket.put("deviceid", deviceid);

        Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
        while (importerIterator.hasNext()) {
            Importer importer = importerIterator.next();
            importer.sendCommand(deviceid,command,param);
        }
    }

    public void init() {

        String telemetryTopic = getPrefix() + "/command";
        ts = new MQTTTopicSubPub(gethost(), "mqttimporter_", getUser(), getPassword(), telemetryTopic, new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean b, String s) {
                if (ts != null) {
                    ts.subscribe(telemetryTopic,0);
                    //ts.subscribe(attributesTopic);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.error("cconnecttion lost");

            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Called when a message arrives from the server that
                // matches any subscription made by the client
                String msg = new String(message.getPayload());
                JSONObject json = new JSONObject(msg);
                if (!json.has("command")) {
                    LOGGER.error("missing command");
                    return;
                }
                String command = json.getString("command");
                LOGGER.info("command: ", command);

                JSONObject jsonPacket = new JSONObject();
                jsonPacket.put("command", command);
                if (!json.has("deviceid")) {
                    return;
                }
                String deviceid = json.getString("deviceid");
                LOGGER.info("deviceid: ", deviceid);
                jsonPacket.put("deviceid", deviceid);
                if (!json.has("param")) {
                    return;
                }
                JSONObject param = json.getJSONObject("param");
                LOGGER.info("param: ", param.toString());
                jsonPacket.put("param", param);

                publishBackCommand(deviceid,command,param);
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        Thread thread = new Thread(ts);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
