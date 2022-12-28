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
		String id = json.getString("id");
		String model = json.getString("model");
		String mac = json.getString("mac");
		Boolean new_fw = json.getBoolean("new_fw");
		String fw_ver = json.getString("fw_ver");

		Iterator<Device> deviceIterator = devices.iterator();

		while (deviceIterator.hasNext()) {
			Device device = deviceIterator.next();
			if (device.getId().equals(id)) {
				LOGGER.info("device " + id + " already registered");
				return null;
			}
		}
		Device newDevice;
		if (model.equals("SHSW-1")) {
			newDevice = new Shelly1(json);
			addDevice(newDevice);

		} else if (model.equals("SHSW-25")) {
			newDevice = new Shelly25(json);
			addDevice(newDevice);

		} else if (model.equals("SHSW-PM")) {
			newDevice = new Shelly1PM(json);
			addDevice(newDevice);

		}else {
			LOGGER.error("cannot create device");

			return null;
		}
		LOGGER.info("device created");

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