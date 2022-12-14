package importer;

import config.Configuration;
import device.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Importer {
    private String name;
    private String importer;
    private String host;
    private String user;
    private String password;
    private String port;
    private String DBname;

    private static List<Device> devices;

    public Importer() {

    }

    public Importer(Importer importer) {
        this.name = importer.name;
        this.importer = importer.importer;
        this.host = importer.host;
        this.user = importer.user;
        this.password = importer.password;
        this.devices = importer.devices;
    }

    public Importer(String name, String importer) {
        super();
        this.name = name;
        this.importer = importer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String gethost() {
        return host;
    }

    public void setThost(String host) {
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

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public static List<Device> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        return devices;
    }

    public void setDevices(List<Device> devices) {
        if (devices == null) {
            return;
            //devices = new ArrayList<>();
        }

        this.devices = new ArrayList<>();
        //Configuration.devices = new ArrayList<>();
        Iterator<Device> deviceIterator = devices.iterator();
        while (deviceIterator.hasNext()) {
            Device device = deviceIterator.next();
            if (device.getType().equals("shelly25")) {
                Shelly25 newDevice = new Shelly25(device);
                this.devices.add(newDevice);
            } else if (device.getType().equals("Shelly1PM")) {
                Shelly1PM newDevice = new Shelly1PM(device);
                this.devices.add(newDevice);
            } else if (device.getType().equals("Shelly4PMPRO")) {
                Shelly4PMPRO newDevice = new Shelly4PMPRO(device);
                this.devices.add(newDevice);
            } else if (device.getType().equals("Shelly1PMPRO")) {
                Shelly1PMPRO newDevice = new Shelly1PMPRO(device);
                this.devices.add(newDevice);
            } else if (device.getType().equals("shellyplugs")) {
                ShellyPlugS newDevice = new ShellyPlugS(device);
                this.devices.add(newDevice);
            } else if (device.getType().equals("laneagent")) {
                LaneAgentDevice newDevice = new LaneAgentDevice(device);
                this.devices.add(newDevice);
            } else {
                System.out.println("Error: Unknown device type: " + device.getType());
            }
        }
    }

    public void init() {
    }
}
