package device;

import importer.ShelliesMQTTImporter;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly25 extends ShellyPM {
    public Shelly25(Device device) {
        super(device);
    }
    public Shelly25(JSONObject json) {
        super(json);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        String command = topic.replace(ShelliesMQTTImporter.prefix, "");
        command = command.replace(getId() + "/", "");
        super.receiveMessage(localDateTime, topic, message);
    }
}
