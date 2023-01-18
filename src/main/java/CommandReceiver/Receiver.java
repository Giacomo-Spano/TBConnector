package CommandReceiver;

import config.Configuration;
import device.Device;
import importer.Importer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Iterator;

public class Receiver {
    private static final Logger LOGGER = LogManager.getLogger(Receiver.class);
    private String name;
    private String receiver;
    private String host;
    private String user;
    private String password;
    private String port;
    private String prefix;
    private String topic;

    public Receiver() {
    }

    public Receiver(Receiver receiver) {
        this.name = receiver.name;
        this.receiver = receiver.receiver;
        this.host = receiver.host;
        this.user = receiver.user;
        this.password = receiver.password;
        this.prefix = receiver.prefix;
        this.topic = receiver.topic;
        this.port = receiver.port;
    }

    public Receiver(String name, String receiver, String host, String user, String password, String topic, String prefix, String port) {
        this.name = name;
        this.receiver = receiver;
        this.host = host;
        this.user = user;
        this.password = password;
        this.prefix = prefix;
        this.topic = topic;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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

    public void receiveCommand(String deviceid, String command, JSONObject param) {
    }

    public void sendCommand(String deviceid, String command, JSONObject param) {

        LOGGER.info("sendCommand");
        Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
        while (importerIterator.hasNext()) {
            Importer importer = importerIterator.next();
            LOGGER.info("publish command to exporter" + importer.getName());
            importer.sendCommand(deviceid, command, param);
        }
        LOGGER.info("command sent");

        /*LOGGER.info("sendCommand");
        Iterator<Transmitter> transmitterIterator = Configuration.getTransmitters().iterator();
        while (transmitterIterator.hasNext()) {
            Transmitter transmitter = transmitterIterator.next();
            LOGGER.info("publish attributes to exporter" + transmitter.getName());
            transmitter.publishCommand(deviceid, command, param);
        }
        LOGGER.info("command sent");*/




    }
}
