package device;

import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly1PM extends ShellyPM {

    public Shelly1PM(Device device) {
        super(device);
    }
    public Shelly1PM(JSONObject json) {
        super(json);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getId() + "/", "");
        super.receiveMessage(localDateTime, topic, message);
    }

}
