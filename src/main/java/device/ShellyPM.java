package device;

import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class ShellyPM extends Shelly {
    private static final Logger LOGGER = LogManager.getLogger(ShellyPM.class);
    private String powerTopic = "relay/0/power"; //"relay/0/power";
    public ShellyPM(Device device) {
        super(device);
        powerTopic = "shellies/" + getName() + "/" + powerTopic;
    }
    public ShellyPM(JSONObject json) {
        super(json);
    }


    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getName() + "/", "");

        super.receiveMessage(localDateTime, topic, message);
        if (command.equals(powerTopic)) {
            receivePowerMessage(localDateTime, topic, message);
        } else {
            LOGGER.info("Topic not found");
        }
    }

    public void receivePowerMessage(LocalDateTime time, String topic, String message) {
        LOGGER.info(
                "\nReceived a Message!" + "\n\tTime:    " + time + "\n\tTopic:   " + topic + "\n\tMessage: "                        + message + "\n");
        double power = Double.valueOf(message);
        //tring publishMsg = "{\"power\":\"" + power + "\"}";
        JSONObject json = new JSONObject(/*publishMsg*/);
        json.put("deviceid", getId());
        json.put("name", getName());
        json.put("type", getType());
        json.put("power", power);

        publishPowerMessage(json);
    }
}