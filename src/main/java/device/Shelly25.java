package device;

import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly25 extends ShellyPM {
    private static final Logger LOGGER = LogManager.getLogger(ShellyPlugS.class);
    public Shelly25(Device device) {
        super(device);
    }
    public Shelly25(JSONObject json) {
        super(json);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {

        LOGGER.info("receiveMessage - topic:" + topic + ", message:" + message);
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getId() + "/", "");
        super.receiveMessage(localDateTime, topic, message);
    }
}
