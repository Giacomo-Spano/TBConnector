//package com.baeldung.jackson.yaml; XXXXXX

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

	// device MQTT setting
	private static String deviceMQTThost;
	private static String deviceMQTTuser;
	private static String deviceMQTTpassword;

	// ThihngsBoard settings
	private static String thingsboardMQTThost;
	private static String thingsboardMQTTPublishTopic;

	// PostGres settings
	private static String postgresURL;

	private static String postgresDBName;

	private static String postgresPort;
	private static String postgresUser;
	private static String postgresPassword;

    // Devices settings
    List<Device> devices;

    public Configuration() {

    }

    public Configuration(String postgresURL, String postgresPort, String postgresDBName, String postgresUser, String postgresPassword,
						 	String deviceMQTThost, String deviceMQTTuser, String deviceMQTTpassword, String thingsboardMQTThost,
    						String thingsboardMQTTPublishTopic, List<Device> devices) {
        super();
		this.postgresURL = postgresURL;
		this.postgresPort = postgresPort;
		this.postgresDBName = postgresDBName;
		this.postgresUser = postgresUser;
		this.postgresPassword = postgresPassword;
        this.deviceMQTThost = deviceMQTThost;
        this.deviceMQTTuser = deviceMQTTuser;
        this.deviceMQTTpassword = deviceMQTTpassword;
        this.thingsboardMQTThost = thingsboardMQTThost;
        this.setThingsboardMQTTPublishTopic(thingsboardMQTTPublishTopic);   
        this.devices = devices;
    }
    
    public String getDeviceMQTThost() {
		return deviceMQTThost;
	}

	public void setDeviceMQTThost(String deviceMQTThost) {
		this.deviceMQTThost = deviceMQTThost;
	}

	public String getDeviceMQTTuser() {
		return deviceMQTTuser;
	}

	public void setDeviceMQTTuser(String deviceMQTTuser) {
		this.deviceMQTTuser = deviceMQTTuser;
	}

	public String getDeviceMQTTpassword() {
		return deviceMQTTpassword;
	}

	public void setDeviceMQTTpassword(String deviceMQTTpassword) {
		this.deviceMQTTpassword = deviceMQTTpassword;
	}

	public static String getThingsboardMQTThost() {
		return thingsboardMQTThost;
	}

	public void setThingsboardMQTThost(String thingsboardMQTThost) {
		this.thingsboardMQTThost = thingsboardMQTThost;
	}

	public static String getThingsboardMQTTPublishTopic() {
		return thingsboardMQTTPublishTopic;
	}

	public void setThingsboardMQTTPublishTopic(String thingsboardMQTTPublishTopic) {
		this.thingsboardMQTTPublishTopic = thingsboardMQTTPublishTopic;
	}

	public static String getPostgresURL() {
		return postgresURL;
	}

	public void setPostgresURL(String postgresURL) {
		this.postgresURL = postgresURL;
	}

	public static String getPostgresDBName() {
		return postgresDBName;
	}

	public void setPostgresDBName(String postgresDBName) {
		this.postgresDBName = postgresDBName;
	}

	public static String getPostgresPort() {
		return postgresPort;
	}

	public void setPostgresPort(String postgresPort) {
		this.postgresPort = postgresPort;
	}

	public static String getPostgresUser() {
		return postgresUser;
	}

	public void setPostgresUser(String postgresUser) {
		this.postgresUser = postgresUser;
	}

	public static String getPostgresPassword() {
		return postgresPassword;
	}

	public void setPostgresPassword(String postgresPassword) {
		this.postgresPassword = postgresPassword;
	}

	/*public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
	public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }*/

    public List<Device> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        return devices;
    }

    public void setDevices(List<Device> devices) {
        if (devices == null) {
        	devices = new ArrayList<>();
        }

        //this.devices = devices;
		this.devices = new ArrayList<>();
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
			} else {
				System.out.println("Error: Unknown device type: " + device.getType());
			}
		}
    }

    @Override
    public String toString() {
        return "Order [orderNo=  ]";
    }

}