	package device;//package com.baeldung.jackson.yaml;

	import config.Configuration;
	import exporter.Exporter;
	//import exporter.PrometheusPublisher;
	import org.apache.logging.log4j.LogManager;
	import org.apache.logging.log4j.Logger;
	import org.eclipse.paho.client.mqttv3.*;
    import org.json.JSONObject;

	import java.time.LocalDateTime;
	import java.util.Iterator;

	public class Device {
		private static final Logger LOGGER = LogManager.getLogger(Device.class);
		private String id = "";
		private String fw_ver = "";
		//private String model = "";
		private String mac = "";
		private boolean new_fw = false;
		private String name;
		private String friendlyname;
		private String type;

		//private String token;
		private String powertopic;

		public Device(JSONObject json) {
			this.id = json.getString("mac");
			this.name = json.getString("id");
			this.type = json.getString("model");
			this.mac = json.getString("mac");
			this.new_fw = json.getBoolean("new_fw");
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

			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				exporter.publishAttributes(mac,json);
			}
		}

		public void publishPowerMessage(LocalDateTime localDateTime, double power) {

			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				exporter.publishPowerMetric(name, type, getId(), localDateTime, power);
			}
		}

		public void publishPowerMessage(JSONObject json) {

			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				exporter.publishPowerMetric(json);
			}
		}

		public void publishStatusMessage(LocalDateTime localDateTime, double status) {

			Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
			while (exporterIterator.hasNext()) {
				Exporter exporter = exporterIterator.next();
				exporter.publishStatusMetric(name, type, localDateTime, status);
			}
		}

		public void receiveExternalCommand(JSONObject json) {

		}
	}