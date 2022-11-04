//package com.baeldung.jackson.yaml; XXXXXX

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {
	private static String deviceMQTThost;
	private static String deviceMQTTuser;
	private static String deviceMQTTpassword;
	private static String thingsboardMQTThost;
	private static String thingsboardMQTTPublishTopic;
	
   /* private String orderNo;
    private LocalDate date;
    private String customerName;*/
    List<Device> devices;

    public Configuration() {

    }

    public Configuration(String deviceMQTThost, String deviceMQTTuser, String deviceMQTTpassword, String thingsboardMQTThost,
    					String thingsboardMQTTPublishTopic, List<Device> devices) {
        super();
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