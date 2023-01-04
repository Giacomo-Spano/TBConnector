package importer;//  aaa

import device.Device;
import exporter.MQTTExporter;
import helper.MQTTTopicSubscriber;
import helper.MQTTWebsocketTopicSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class MQTTImporter extends Importer {

    private static final Logger LOGGER = LogManager.getLogger(MQTTImporter.class);

    public MQTTImporter(Importer importer) {
        super(importer);
    }

    private MQTTTopicSubscriber ts;

    public void init() {

        String telemetryTopic = getPrefix() + "/attributes";//MQTTExporter.prefix + "/update/attributes";
        String attributesTopic = getPrefix() + "/telemetry";
        ts = new MQTTTopicSubscriber(gethost(), "mqttimporter_", getUser(), getPassword(), attributesTopic, new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean b, String s) {
                if (ts != null) {
                    ts.subscribe(telemetryTopic);
                    ts.subscribe(attributesTopic);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                LOGGER.error("cconnecttion lost");
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Called when a message arrives from the server that
                // matches any subscription made by the client
                String msg = new String(message.getPayload());
                JSONObject json = new JSONObject(msg);
                String command = json.getString("command");
                LOGGER.info("command: ", command);
                if (command.equals("pushattributes")) {
                    LOGGER.info("command pushattribute found ");
                    String deviceid = json.getString("mac");
                    LOGGER.info("deviceid: ", deviceid);
                    Device device = getDeviceFromId(deviceid);
                    if (device == null)
                        device = registerNewDevice(json);
                    if (device != null) {
                        //JSONObject js = new JSONObject(json);
                        if (json.has("command"))
                            json.remove("command");
                        device.publishAttributes(json);
                    }
                } else if (command.equals("pushtelemetry")) {
                    LOGGER.info("command pushtelemetry found ");
                    if (!json.has("deviceid")) {
                        LOGGER.error("cannot find deviceid");
                        return;
                    }
                    String deviceid = json.getString("deviceid");
                    LOGGER.info("deviceid: ", deviceid);
                    Device device = getDeviceFromId(deviceid);
                    if (device == null) {
                        // if device does not exist create a new one
                        if (!json.has("type")) {
                            LOGGER.error("cannot create new device");
                            return;
                        }
                        String type = json.getString("type");
                        if (json.has("name"));
                            String id = json.getString("name");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("mac",deviceid );
                        jsonObject.put("model",type );
                        jsonObject.put("id",id );
                        device = registerNewDevice(jsonObject);
                    }
                    if (device != null) {
                        //JSONObject js = new JSONObject(json);
                        if (json.has("command"))
                            json.remove("command");
                        if (json.has("deviceid"))
                            json.remove("deviceid");
                        if (json.has("type"))
                            json.remove("type");
                        if (json.has("name"))
                            json.remove("name");
                        device.publishTelemetryMessage(json);
                    }
                } else {
                    LOGGER.info("command not found ");
                    registerNewDevice(json);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        ts.subscribe(telemetryTopic);

        Thread thread = new Thread(ts);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    protected Device registerNewDevice(JSONObject json) {
        Device newDevice = super.registerNewDevice(json);
        if (newDevice == null) {
            LOGGER.error("Cannot create new device");
            return null;
        }
        return newDevice;
    }
}