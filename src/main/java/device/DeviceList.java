package device;//package com.baeldung.jackson.yaml; XXXXXX

import helper.MQTTTopicSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeviceList {
	private static final Logger LOGGER = LogManager.getLogger(DeviceList.class);

	private List<Device> devices = new ArrayList<>();

	private List<DeviceListener> listeners = new ArrayList<DeviceListener>();

	public void addListener(DeviceListener toAdd) {
		listeners.add(toAdd);
	}

	public interface DeviceListener {
		void deviceAdded(Device newDevice);
	}


	public void init() {
	}
	public Device registerNewDevice(JSONObject json) {
		LOGGER.info("registerNewDevice:" + json.toString());
		if (!json.has("mac")) {
			LOGGER.error("missing mac - cannot create device");
			return null;
		}
		if (!json.has("model")) {
			LOGGER.error("missing model - cannot create device");
			return null;
		}
		String id = json.getString("mac");
		LOGGER.info("id:" + id);
		String model = json.getString("model");
		LOGGER.info("model:" + model);
		Iterator<Device> deviceIterator = devices.iterator();
		while (deviceIterator.hasNext()) {
			Device device = deviceIterator.next();
			if (device.getId().equals(id)) {
				LOGGER.info("device " + id + " already registered");
				return device;
			}
		}
		Device newDevice;
		if (model.equals("SHSW-1")) {
			newDevice = new Shelly1(json);
			addDevice(newDevice);
		} else if (model.equals("SHSW-25")) {
			newDevice = new Shelly25(json);
			addDevice(newDevice);
		} else if (model.equals("SHPLG-S")) {
			newDevice = new ShellyPlugS(json);
			addDevice(newDevice);
		} else if (model.equals("SHSW-PM")) {
			newDevice = new Shelly1PM(json);
			addDevice(newDevice);
		} else {
			LOGGER.error("cannot create device");
			return null;
		}
		LOGGER.info("device created");
		LOGGER.info("id:" + newDevice.getId());
		LOGGER.info("type:" + newDevice.getType());
		LOGGER.info("name:" + newDevice.getName());
		return newDevice;
	}

	private void addDevice(Device newDevice) {
		devices.add(newDevice);

		for (DeviceListener hl : listeners)
			hl.deviceAdded(newDevice);
	}

	public List<Device> getDevices() {
		return devices;
	}
}