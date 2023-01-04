	package device;//package com.baeldung.jackson.yaml;

	import config.Configuration;
	import exporter.Exporter;
	//import exporter.PrometheusPublisher;
	import helper.MQTTTopicSubscriber;
	import org.apache.logging.log4j.LogManager;
	import org.apache.logging.log4j.Logger;
	import org.eclipse.paho.client.mqttv3.*;
    import org.json.JSONObject;

	import java.time.LocalDateTime;
	import java.util.Iterator;

	public class Device {

		public MQTTTopicSubscriber mqttTopicSubscriber;
		private static final Logger LOGGER = LogManager.getLogger(Device.class);
		private String id = "";
		private String fw_ver = "";
		private String mac = "";
		private boolean new_fw = false;
		private String name;
		private String friendlyname;
		private String type;
		private String powertopic;

		public Device(JSONObject json) {
			if (!json.has("mac") || !json.has("model")) {
				throw new RuntimeException("missing required field");
			} else {
				this.id = json.getString("mac");
				this.type = json.getString("model");
			}
			if (json.has("id"))
				this.name = json.getString("id");
			if (json.has("mac"))
				this.mac = json.getString("mac");
			if (json.has("new_fw"))
				this.new_fw = json.getBoolean("new_fw");
			if (json.has("fw_ver"))
				this.fw_ver = json.getString("fw_ver");
		}

		public MqttCallback createCallback() {
			return new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable cause) {
				}
			};
		}

		public Device() {

		}

		public Device(Device device) {
			this.name = device.name;
			this.friendlyname = device.friendlyname;
			this.type = device.type;
			this.id = device.id;
			//this.token = device.token;
			this.powertopic = device.powertopic;
		}
		public Device(String name, String friendlyname, String type, String token) {
			super();
			this.name = name;
			this.friendlyname = friendlyname;
			this.type = type;
			//this.token = token;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFriendlyname() {
			return friendlyname;
		}

		public void setFriendlyname(String name) {
			this.friendlyname = friendlyname;
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

		public String getPowertopic() {
			return powertopic;
		}

		public void setPowertopic(String powertopic) {
			this.powertopic = powertopic;
		}

		@Override
		public String toString() {
			return "Devicee [name=" + name + ", type=" + type + ", token=" + id + "]";
		}

		public void receiveMessage(LocalDateTime localDateTime, String topic, String message) {
			LOGGER.info("receiveMessage");
		}

		public void subscribeTopics(MqttClient mqttClient) {
			LOGGER.info("subscribeTopics");
			//mqttClient.setCallback(createCallback());
			try {
				mqttClient.subscribe(powertopic, 0);
			} catch (MqttException e) {
				throw new RuntimeException(e);
			}
		}

		public void publishAttributes(JSONObject json) {
			LOGGER.info("publishAttributes");
			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				LOGGER.info("publish attributes to exporter" + exporter.getName());
				exporter.publishAttributes(mac,json);
			}
			LOGGER.info("Attributes published");
		}

		public void publishTelemetryMessage(LocalDateTime localDateTime, double power) {
			LOGGER.info("publishTelemetry");
			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				LOGGER.info("publish telemetry to exporter" + exporter.getName());
				exporter.publishTelemetry(name, type, getId(), localDateTime, power);
			}
			LOGGER.info("Telemetry published");
		}

		public void publishTelemetryMessage(JSONObject json) {
			LOGGER.info("publishTelemetry");
			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				LOGGER.info("publish telemetry to exporter" + exporter.getName());
				exporter.publishTelemetry(json,getId(),getType());
			}
			LOGGER.info("Telemetry published");
		}

		public void publishStatusMessage(LocalDateTime localDateTime, double status) {
			LOGGER.info("publishStatusMessage");
			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				LOGGER.info("publish Status to exporter" + exporter.getName());
				exporter.publishStatusMetric(name, type, localDateTime, status);
			}
			LOGGER.info("Status published");
		}

		public void receiveExternalCommand(JSONObject json) {

		}
	}