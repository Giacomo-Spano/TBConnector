package importer;//  aaa

import device.Device;
import device.DeviceList;
import device.Shelly;
import emulator.Emulator;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Mqtt topic subscriber
 */
public class ShelliesMQTTImporter extends Importer {

    private static final Logger LOGGER = LogManager.getLogger(ShelliesMQTTImporter.class);

    public static String prefix = "shellies/";

    private String shelliesTopic = "shellies/#";


    private MQTTTopicSubscriber ts;

    public ShelliesMQTTImporter(Importer importer) {
        super(importer);
    }

    public void init() {

        ts = new MQTTTopicSubscriber(gethost(), "shelliesimporter_", getUser(), getPassword(), shelliesTopic, new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                if (ts != null) {
                    ts.subscribe(shelliesTopic,0);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Called when a message arrives from the server that
                // matches any subscription made by the client
                String msg = new String(message.getPayload());
                LOGGER.info("Message CommandReceiver.command received -  topic: " + topic + "message: " + msg);

                String str = topic.replace(prefix, "");
                int index = str.indexOf("/");
                String deviceid = null;
                if (index != -1) {
                    deviceid = str.substring(0, index);
                    String command = str.replace(deviceid + "/", "");
                    LOGGER.info("deviceid: ", deviceid + "CommandReceiver.command: ", command);
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

        //publishAnnounce();
        new Timer().schedule(new SendAnnounceTask(), 10000, 5000); // delay 10 sec
    }

    private class SendAnnounceTask extends TimerTask {
        @Override
        public void run() {

            publishAnnounce();
            cancel();
        }
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
        ((Shelly)newDevice).setMQTTdata(gethost(),getPrefix(),getUser(),getPassword());
        ((Shelly)newDevice).subscribeDeviceMessages();

        return newDevice;
    }

    public void sendCommand(String deviceid, String command, JSONObject param) {
        Shelly device = (Shelly) getDeviceFromId(deviceid);
        if (device == null)
            return;
        device.sendCommand(command, param);

    }
}