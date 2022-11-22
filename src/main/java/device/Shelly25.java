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

        //String power = message;
        double power = Double.valueOf(message);

        //PostgresPublisher postgresPublisher = new PostgresPublisher();
        //postgresPublisher.publish(getId(),getName(),Float.valueOf(message));
        publishPowerMessage(localDateTime, power);


        /*try {
            String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
            String publishMsg = "{\"power\":\"" + message + "\"}";

            TopicPublisher publisher = new TopicPublisher();
            publisher.createConnection(Configuration.getThingsboardMQTThost(), getToken(),"");
            publisher.publishMessage(publishTopic, publishMsg);
            publisher.closeConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
}
