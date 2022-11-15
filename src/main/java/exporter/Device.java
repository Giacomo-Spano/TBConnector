package exporter;//package com.baeldung.jackson.yaml;

import org.json.JSONObject;

import java.time.LocalDateTime;

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

		JSONObject jo = new JSONObject();
		jo.put("time", localDateTime.toString());
		jo.put("power", power);

		//publishPostgresPowerMessage(localDateTime,power);

		//publishMQTTPowerMessage(localDateTime,power);

		PrometheusPublisher.publishPowerMetric(localDateTime,power);
	}

	public void publishPostgresPowerMessage(LocalDateTime localDateTime, String power) {
		PostgresPublisher postgresPublisher = new PostgresPublisher();
		postgresPublisher.publish(localDateTime, getId(), getName(), Float.valueOf(power));
	}

	public void publishMQTTPowerMessage(LocalDateTime localDateTime, String power) {
		try {
			String publishTopic = Configuration.getThingsboardMQTTPublishTopic();//"v1/devices/me/telemetry";
			String publishMsg = "{\"power\":\"" + power + "\"}";

			TopicPublisher publisher = new TopicPublisher();
			publisher.createConnection(Configuration.getThingsboardMQTThost(), getToken(),"");
			publisher.publishMessage(publishTopic, publishMsg);
			publisher.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publishPrometheus(LocalDateTime localDateTime, String power) {

	}
}