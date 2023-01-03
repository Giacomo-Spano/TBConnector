package device;

import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly extends Device {
    private static final Logger LOGGER = LogManager.getLogger(Shelly.class);

    private String infoTopic = "info";
    public Shelly(Device device) {
        super(device);
    }

    public Shelly(JSONObject json) {
        super(json);
    }

    public void subscribeTopics(MqttClient mqttClient) {
        LOGGER.info("subscribeTopics");
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
            LOGGER.warn("Topic \"" + topic + "\" not found");
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
}
