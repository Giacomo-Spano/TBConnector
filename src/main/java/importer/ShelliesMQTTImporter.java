package importer;//  aaa

import device.Device;
import device.DeviceList;
import helper.MQTTTopicPublisher;
import helper.MQTTTopicSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Mqtt topic subscriber
 */
public class ShelliesMQTTImporter extends Importer {

    private static final Logger LOGGER = LogManager.getLogger(ShelliesMQTTImporter.class);

    public static String prefix = "shellies/";

    public ShelliesMQTTImporter(Importer importer) {
        super(importer);
    }

    public void init() {

        MQTTTopicSubscriber ts = new MQTTTopicSubscriber(gethost(), "shelliesimporter_", getUser(), getPassword(), "shellies/#", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Called when a message arrives from the server that
                // matches any subscription made by the client
                String msg = new String(message.getPayload());
                LOGGER.info("Message command received -  topic: " + topic + "message: " + msg);

                String str = topic.replace(prefix, "");
                int index = str.indexOf("/");
                String deviceid = null;
                if (index != -1) {
                    deviceid = str.substring(0, index);
                    String command = str.replace(deviceid + "/", "");
                    LOGGER.info("deviceid: ", deviceid + "command: ", command);
                    if (command.equals("announce")) {
                        LOGGER.info("command announce found ");
                        JSONObject json = new JSONObject(msg);
                        Device newDevice = registerNewDevice(json);
                        if (newDevice != null)
                            newDevice.publishAttributes(json);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        Thread thread = new Thread(ts);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        publishAnnounce();
    }

    public void subscribeShelliesTopic(MqttClient mqttClient) {
        LOGGER.info("subscribeAvailabilityTopic");
        try {
            mqttClient.subscribe("shellies/#", 0);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishAnnounce() {

        try {
            String publishTopic = "shellies/command";
            String publishMsg = "announce";

            MQTTTopicPublisher publisher = new MQTTTopicPublisher();
            String clientID = "HelloWorldPub_" + UUID.randomUUID().toString().substring(0, 8);
            if (publisher.createConnection(gethost(), clientID, getUser(), getPassword())) {
                publisher.publishMessage(publishTopic, publishMsg);
                publisher.closeConnection();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected Device registerNewDevice(JSONObject json) {
        Device newDevice = super.registerNewDevice(json);
        if (newDevice == null) {
            LOGGER.error("Cannot create new device");
            return null;
        }
        //newDevice.publishAttributes(json);
        LOGGER.info("subscribe to new device messages");
        MQTTTopicSubscriber ts = new MQTTTopicSubscriber(gethost(), "shimporter" + newDevice.getId() + "_", getUser(), getPassword(), "shellies/" + newDevice.getName() + "/#", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.error("connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                LOGGER.info("Message topic: " + topic + " payload: " + mqttMessage.getPayload() +"arrived");
                LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
                System.out.println(localDateTime);
                String time = new Timestamp(System.currentTimeMillis()).toString();
                newDevice.receiveMessage(localDateTime, topic, new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                LOGGER.info("deliveryComplete ");
            }
        });
        Thread thread = new Thread(ts);
        thread.start();
        LOGGER.info("subscribed to new device messages ");
        return newDevice;
    }
}