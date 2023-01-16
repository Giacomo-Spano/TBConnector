package device;

import helper.MQTTTopicPublisher;
import helper.MQTTTopicSubscriber;
import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class Shelly extends Device {
    private static final Logger LOGGER = LogManager.getLogger(Shelly.class);

    String host;
    String prefix;
    String user;
    String password;

    private String infoTopic = "info";
    public Shelly(Device device) {
        super(device);
    }

    public Shelly(JSONObject json) {
        super(json);

    }

    public void setMQTTdata(String host,String prefix, String user,String password) {
        LOGGER.info("setMQTTdata");
        this.host = host;
        if (prefix == null) {
            LOGGER.warn("prefix is null");
            prefix = "";
        }
        this.prefix = prefix;
        this.user = user;
        this.password = password;
    }

    public void subscribeDeviceMessages() {
        LOGGER.info("subscribeDeviceMessages");
        LOGGER.info("subscribe to new device messages");
        String shellyTopic = prefix + this.getName() + "/#";
        this.mqttTopicSubscriber = new MQTTTopicSubscriber(host, "shimporter" + this.getId() + "_",user, password, shellyTopic, new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                mqttTopicSubscriber.subscribe(shellyTopic,0);
            }

            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.error("connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                LOGGER.info("Message topic: " + topic + " payload: " + mqttMessage.getPayload() +"arrived");
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
                System.out.println(localDateTime);
                String time = new Timestamp(System.currentTimeMillis()).toString();
                receiveMessage(localDateTime, topic, new String(mqttMessage.getPayload()));
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

    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        LOGGER.info("receiveMessage - topic:" + topic + ", message:" + message);
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getId() + "/", "");
        LOGGER.info("command:" + command);
        if (command.equals("announce")) {
            JSONObject json = new JSONObject(message);
            receiveAnnounce(json);
        } else if (command.equals(infoTopic)) {
            receiveInfo(localDateTime, topic, message);
        } else {
            super.receiveMessage(localDateTime, topic, message);
        }
    }

    public void receiveAnnounce(JSONObject json) {
        LOGGER.info("\nReceived info Message!" + json.toString());
        publishAttributes(json);
    }
    public void receiveInfo(LocalDateTime time, String topic, String message) {
        LOGGER.info(
                "\nReceived info Message!" + "\n\tTime:    " + time + "\n\tTopic:   " + topic + "\n\tMessage: "                        + message + "\n");
    }

    public void sendCommand(String command, JSONObject param) {
        LOGGER.info("sendCommand");
        if (param != null)
            LOGGER.info("param: " + param.toString());
        if (command.equals("announce")) {
            sendAnnounceCommand();
        }
    }
    public void sendAnnounceCommand() {
        LOGGER.info("sendAnnounceCommand");
        try {
            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "announcecommand_" + UUID.randomUUID().toString().substring(0,8);
            if (publisher.createConnection(host, clientID, user, password)) {
                String topic = prefix + getId() + "/command";
                publisher.publishMessage(topic, "announce");
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
