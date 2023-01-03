package device;

import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class ShellyPlugS extends ShellyPM {
    private static final Logger LOGGER = LogManager.getLogger(ShellyPlugS.class);
    public ShellyPlugS(Device device) {
        super(device);
    }

    public ShellyPlugS(JSONObject json) {
        super(json);
    }

    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        LOGGER.info("receiveMessage - topic:" + topic + ", message:" + message);
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getId() + "/", "");
        LOGGER.info("command:" + command);
        super.receiveMessage(localDateTime, topic, message);
    }
}
