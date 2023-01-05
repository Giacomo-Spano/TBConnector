package CommandControllers;

import command.Command;
import config.Configuration;
import device.Device;
import device.DeviceList;
import helper.MQTTTopicSubscriber;
import importer.Importer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Iterator;

public class MQTTCommandController extends CommandController implements DeviceList.DeviceListener {
    private static final Logger LOGGER = LogManager.getLogger(MQTTCommandController.class);

    /*public MQTTCommandController() {

        if (Configuration.getImporters() != null && Configuration.getImporters().size() > 0) {
            Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
            while (importerIterator.hasNext()) {
                Importer importer = importerIterator.next();
                importer.getDevicesList().addListener(this);
            }
        }
    }*/

    public MQTTCommandController(CommandController controller) {
        super(controller);
    }

    public void execute(JSONObject json) {

    }

    @Override
    public void deviceAdded(Device newDevice) {
        // subscribe to MQTT  to receive command request sent to devices
        String host = gethost();
        LOGGER.info("host: " + host);
        String clientId = "mqttcontroller_" + newDevice.getId();
        LOGGER.info("clientId: " + clientId);
        String user = getUser();
        LOGGER.info("user: " + user);
        String password = getPassword();
        LOGGER.info("password: " + password);
        String prefix = getPrefix();
        LOGGER.info("prefix: " + prefix);
        String topic = getPrefix() + getTopic();
        LOGGER.info("topic: " + topic);
        MQTTTopicSubscriber ts = new MQTTTopicSubscriber(host, clientId, user, password, topic, new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                String id = newDevice.getId();
                LOGGER.error("connection lost " + newDevice.getId());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                String topic = s;
                String message = new String(mqttMessage.getPayload());
                JSONObject json;
                try {
                    json = new JSONObject(message);

                if (json.has("deviceid") && json.has("command")) {
                    String deviceid = json.getString("deviceid");
                    String command = json.getString("command");
                    JSONObject param = null;
                    if (json.has("param")) {
                        param = json.getJSONObject("param");
                    }
                    sendCommand(deviceid,command,param);
                }
                } catch (Exception e) {
                    LOGGER.error("JSON error");
                    return;
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        Thread thread = new Thread(ts);
        thread.start();
    }

    public void init() {


        if (Configuration.getImporters() != null && Configuration.getImporters().size() > 0) {
            Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
            while (importerIterator.hasNext()) {
                Importer importer = importerIterator.next();
                importer.getDevicesList().addListener(this);
            }
        }
    }
}
