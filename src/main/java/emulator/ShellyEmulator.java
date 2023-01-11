package emulator;

import helper.MQTTTopicPublisher;
import helper.MQTTTopicSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ShellyEmulator {
    private static final Logger LOGGER = LogManager.getLogger(ShellyEmulator.class);
    private String host = "tcp://153.77.136.201:1883";
    private String user = "giacomo";
    private String password = "giacomo";

    private String name = "emulator";
    private String model = "SHPLG-S";
    private String id = "12345";


    private String telemetryTopic = "shellies/";

    public ShellyEmulator(String id, String name) {
        this.id = id;
        this.name = name;
        init();
    }
    private void init() {

        subscribeDeviceMessages();
        //publishAttributes();

        new Timer().schedule(new SendTelemetryTask(), 0, 5000);

    }

    private class SendTelemetryTask extends TimerTask {
        @Override
        public void run() {

            String topic = "shellies/" + name + "-" + id + "/relay/0/power";
            int randomNum = 100 + ThreadLocalRandom.current().nextInt(0, 9+1);
            publishTelemetry(topic, "" + randomNum);
        }
    }
    public void publishTelemetry(String topic, String msg) {
        LOGGER.info("publish telemetry - json:" + msg);
        try {
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "aaaa_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(host, clientID, user, password)) {
                publisher.publishMessage(topic, msg);
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("failed to publish telemetry" + e.toString());
            e.printStackTrace();
        }
        LOGGER.info("telemetry published");
    }

    public void publishAttributes() {
        LOGGER.info("publish attributes");

        JSONObject json = new JSONObject();
        json.put("id", name + "-" + id);
        json.put("model", model);
        json.put("mac", id);
        json.put("ip", "153.77.139.149");
        json.put("new_fw", true);
        json.put("fw_ver", "20221108-153548/v1.12.1-1PM-fix-g2821131");

        String topic = "shellies/" + name + "-" + id + "/announce";

        try {
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "emupubTelemetry_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(host, clientID, user, password)) {
                publisher.publishMessage(topic, json.toString());
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOGGER.error("failed to publish telemetry" + e.toString());
            e.printStackTrace();
        }
        LOGGER.info("telemetry published");
    }

    public void subscribeDeviceMessages() {
        LOGGER.info("subscribeDeviceMessages");

        LOGGER.info("subscribe to new device messages");
        String commandTopic = "shellies/" + "command";

        MQTTTopicSubscriber mqttTopicSubscriber = new MQTTTopicSubscriber(host, "emulator" + id + "_",user, password, commandTopic, new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                //mqttTopicSubscriber.subscribe(commandTopic);
            }

            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.error("connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                LOGGER.info("Message topic: " + topic + " payload: " + mqttMessage.getPayload() +"arrived");

                String message = new String (mqttMessage.getPayload());
                if( message.equals("announce")) {
                    publishAttributes();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                LOGGER.info("deliveryComplete ");
            }
        });
        Thread thread = new Thread(mqttTopicSubscriber);
        thread.start();
        LOGGER.info("subscribed to new device messages ");
    }

}
