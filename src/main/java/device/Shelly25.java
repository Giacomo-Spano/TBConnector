package device;

import device.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class Shelly25 extends Device {

    private static final Logger LOGGER = LogManager.getLogger(Shelly25.class);

    public Shelly25(Device device) {
        super(device);
    }
    public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {

        LOGGER.info("receive message topic: " + topic + ", message" + message);

        double power = Double.valueOf(message);
        publishPowerMessage(localDateTime, power);

    }
}
