package device;//package com.baeldung.jackson.yaml;

import config.Configuration;
import exporter.Exporter;
//import exporter.PrometheusPublisher;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Iterator;

public class Device {
    
    private String name;
    private String type;
    private String id;
    private String token;
    private String powertopic;

    public Device() {
    }
	public Device(Device device) {
		this.name = device.name;
		this.type = device.type;
		this.id = device.id;
		this.token = device.token;
		this.powertopic = device.powertopic;
	}
    public Device(String name, String type, String token) {
        super();
        this.name = name;
        this.type = type;
        this.token = token;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPowertopic() {
		return powertopic;
	}

	public void setPowertopic(String powertopic) {
		this.powertopic = powertopic;
	}

	@Override
    public String toString() {
        return "Devicee [name=" + name + ", type=" + type + ", token=" + token + "]";
    }

	public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {

	}

	public void publishPowerMessage(LocalDateTime localDateTime, double power) {

		/*JSONObject jo = new JSONObject();
		jo.put("time", localDateTime.toString());
		jo.put("power", power);*/

		Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
		while (exporterIterator.hasNext()) {
			Exporter exporter = exporterIterator.next();
			exporter.publishPowerMetric(name, type, getToken(), localDateTime, power);
		}
	}
}