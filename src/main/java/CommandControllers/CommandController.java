package CommandControllers;

import config.Configuration;
import device.Device;
import device.DeviceList;
import device.Shelly;
import helper.MQTTTopicPublisher;
import importer.Importer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.UUID;

public class CommandController {
    private static final Logger LOGGER = LogManager.getLogger(CommandController.class);
    private String name;
    private String controller;
    private String host;
    private String user;
    private String password;
    private String port;
    private String prefix;
    private String topic;
    public CommandController() {
    }

    public CommandController(CommandController controller) {
        this.name = controller.name;
        this.controller = controller.controller;
        this.host = controller.host;
        this.user = controller.user;
        this.password = controller.password;
        this.prefix = controller.prefix;
        this.topic = controller.topic;
    }

    public CommandController(String name, String controller) {
        super();
        this.name = name;
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String gethost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    protected Device getDeviceFromId(String deviceId) {
        Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
        while (importerIterator.hasNext()) {
            Importer importer = importerIterator.next();
            Iterator<Device> deviceIterator = importer.getDevicesList().getDevices().iterator();
            while (deviceIterator.hasNext()) {
                Device device = deviceIterator.next();
                if (device.getId().equals(deviceId)) {
                    return device;
                }
            }
        }
        return null;
    }
    public void init() {
    }

    public void sendCommand(String deviceid, String command, JSONObject param) {
        LOGGER.info("sendCommand");
        Device device = getDeviceFromId(deviceid);
        if (device != null) {
            device.sendCommand(command,param);
        }
    }


}
