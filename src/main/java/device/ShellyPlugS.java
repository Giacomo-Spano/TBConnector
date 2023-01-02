package device;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class ShellyPlugS extends Device {
    private static final Logger LOGGER = LogManager.getLogger(ShellyPlugS.class);
    public ShellyPlugS(Device device) {
        super(device);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
        LOGGER.info("receive message topic: " + topic + ", message" + message);
        double power = Double.valueOf(message);
        publishTelemetryMessage(localDateTime, power);
    }
}
