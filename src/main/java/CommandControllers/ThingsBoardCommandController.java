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

public class ThingsBoardCommandController extends Command implements DeviceList.DeviceListener {
    private static final Logger LOGGER = LogManager.getLogger(ThingsBoardCommandController.class);

    public ThingsBoardCommandController() {

        if (Configuration.getImporters() != null && Configuration.getImporters().size() > 0) {
            Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
            while (importerIterator.hasNext()) {
                Importer importer = importerIterator.next();
                importer.getDevicesList().addListener(this);
            }
        }
    }

    public void execute(JSONObject json)  {

    }
    @Override
    public void deviceAdded(Device newDevice) {
        // subscribe to ThingsBoard server to get command request
        MQTTTopicSubscriber ts = new MQTTTopicSubscriber("tcp://localhost:1883", "command_" + newDevice.getId(), newDevice.getId(),"", "v1/devices/me/rpc/request/+", new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                String id = newDevice.getId();
                LOGGER.error("connection lost " + newDevice.getId());
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        Thread thread = new Thread(ts);
        thread.start();
    }
}
