    package device;

    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    import org.eclipse.paho.client.mqttv3.*;
    import org.json.JSONObject;

    import java.time.LocalDateTime;

    public class Shelly4PMPRO extends Device {
        private static final Logger LOGGER = LogManager.getLogger(Shelly4PMPRO.class);

        private String statusNotificationTopic = "status/switch:"; //"NOME-SHELLY/status/switch:ID";
        private String availabilityTopic = "online"; //"NOME-SHELLY/online";

        public Shelly4PMPRO(Device device) {
            super(device);
            statusNotificationTopic = getName() + "/" + statusNotificationTopic + getId();
            availabilityTopic = getName() + "/" + availabilityTopic + getId();
        }

        public void subscribeTopics(MqttClient mqttClient) {
            LOGGER.info("subscribeTopics");
            //super.subscribeTopics(mqttClient);
            subscribeAvailabilityTopic(mqttClient);
            subscribeStatusNotificationTopic(mqttClient);
        }

        public void subscribeAvailabilityTopic(MqttClient mqttClient) {
            LOGGER.info("subscribeAvailabilityTopic");
            try {
                mqttClient.subscribe(availabilityTopic, 0);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

        public void subscribeStatusNotificationTopic(MqttClient mqttClient) {
            LOGGER.info("subscribeAvailabilityTopic");
            try {
                mqttClient.subscribe(statusNotificationTopic, 0);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

        public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
            if (topic.equals(statusNotificationTopic)) {
                receiveAvailabilityMessage(localDateTime, topic, message);
            } else if (topic.equals(statusNotificationTopic)) {
                receiveStatusNotificationMessage(localDateTime, topic, message);
            } else {
                LOGGER.info("Topic not found");
            }
        }

        public void receiveAvailabilityMessage(LocalDateTime time, String topic, String message) {
            LOGGER.info(
                "\nReceived a Message!" + "\n\tTime:    " + time + "\n\tTopic:   " + topic + "\n\tMessage: "
                + message + "\n");
        }

        public void receiveStatusNotificationMessage(LocalDateTime time, String topic, String message)  {
            LOGGER.info(
                "\nReceived a Message!" + "\n\tTime:    " + time + "\n\tTopic:   " + topic + "\n\tMessage: "
                + message + "\n");

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

                    publishPowerMessage(time, power);
                }

    }
