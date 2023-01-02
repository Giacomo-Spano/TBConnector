package helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * A Mqtt topic publisher
 */
public class MQTTWebsocketTopicPublisher {
    private static final Logger LOGGER = LogManager.getLogger(MQTTWebsocketTopicPublisher.class);
    static boolean isShutdown = false;
    MqttClient mqttClient;

    public boolean createConnection(String host, String clientId, String user, String password) {
        LOGGER.info("TopicPublisher initializing...");
        String topic = "provatopic";
        String content = "Message from MqttPublishSample";
        int qos = 2;
        String broker = "ws://" + host;
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(user);
            connOpts.setPassword(password.toCharArray());
            LOGGER.info("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            LOGGER.info("Connected");
        } catch (MqttException me) {
            LOGGER.error("reason " + me.getReasonCode());
            LOGGER.error("msg " + me.getMessage());
            LOGGER.error("loc " + me.getLocalizedMessage());
            LOGGER.error("cause " + me.getCause());
            LOGGER.error("excep " + me);
            me.printStackTrace();
            return false;
        }
        return true;
    }

    public void publishMessage(String topic, String content) {

        int qos = 2;

        // Create a Mqtt message
        //String content = "Hello world from MQTT!";
        LOGGER.info("Publishing message. topic: " + topic + " Mesessage:" + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);

        // Publish the message
        try {
            mqttClient.publish(topic, message);
        } catch (MqttPersistenceException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            LOGGER.error(e1.toString());
        } catch (MqttException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            LOGGER.error(e1.toString());
        }
    }

    public void closeConnection() {
        // Disconnect the client
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LOGGER.info("Messages published.");
    }

}