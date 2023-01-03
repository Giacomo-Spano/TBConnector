package device;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class Shelly1PMPRO extends Device {
    private static final Logger LOGGER = LogManager.getLogger(Shelly1PMPRO.class);
    public Shelly1PMPRO(Device device) {
        super(device);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        //https://shelly-api-docs.shelly.cloud/gen2/ComponentsAndServices/Switch/

        LOGGER.info("receive message topic: " + topic + ", message" + message);

        JSONObject jo = new JSONObject(message);
        //Last measured instantaneous active power (in Watts) delivered to the attached load
        // (shown if applicable)Last measured instantaneous active power (in Watts) delivered
        // to the attached load (shown if applicable)
        String strpower = jo.get("apower").toString();
        double power = Double.valueOf(strpower);

        //Last measured voltage in Volts (shown if applicable)
        String voltage = jo.get("voltage").toString();

        // Last measured current in Amperes (shown if applicable)
        String current = jo.get("current").toString();

        publishTelemetryMessage(localDateTime, power);
    }
}
