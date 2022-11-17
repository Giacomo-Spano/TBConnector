package config;//package com.baeldung.jackson.yaml; XXXXXX

import device.Device;
import device.Shelly1PM;
import device.Shelly25;
import device.Shelly4PMPRO;
import exporter.*;
import importer.Importer;
import importer.MQTTImporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

    // Devices settings
	private static List<Device> devices;

	private static List<AgentConfiguration> agent;

	private static List<Exporter> exporters;

	private static List<Importer> importers;


    public Configuration() {

    }

    public Configuration(String thingsboardMQTThost, String thingsboardMQTTPublishTopic, List<Device> devices, List<AgentConfiguration> agent, Exporter exporter) {
        super();

        this.devices = devices;
		this.agent = agent;
    }

    public static List<Device> getDevices() {
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
		Configuration.devices = new ArrayList<>();
		Iterator<Device> deviceIterator = devices.iterator();
		while (deviceIterator.hasNext()) {
			Device device = deviceIterator.next();
			if (device.getType().equals("shelly25")) {
				Shelly25 newDevice = new Shelly25(device);
				Configuration.devices.add(newDevice);
			} else if (device.getType().equals("Shelly1PM")) {
				Shelly1PM newDevice = new Shelly1PM(device);
				Configuration.devices.add(newDevice);
			} else if (device.getType().equals("Shelly4PMPRO")) {
				Shelly4PMPRO newDevice = new Shelly4PMPRO(device);
				Configuration.devices.add(newDevice);
			} else {
				System.out.println("Error: Unknown device type: " + device.getType());
			}
		}
    }

	public static List<AgentConfiguration> getAgent() {
		if (agent == null) {
			agent = new ArrayList<>();
		}
		return agent;
	}

	public void setAgent(List<AgentConfiguration> agent) {
		if (agent == null) {
			agent = new ArrayList<>();
		}
		Configuration.agent = new ArrayList<>();
		Iterator<AgentConfiguration> agentconfIterator = agent.iterator();
		while (agentconfIterator.hasNext()) {
			AgentConfiguration agentConfiguration = agentconfIterator.next();
			AgentConfiguration newac = new AgentConfiguration(agentConfiguration);
			this.agent.add(newac);

		}
	}

	public static List<Exporter> getExporters() {
		if (exporters == null) {
			exporters = new ArrayList<>();
		}
		return exporters;
	}

	public void setExporters(List<Exporter> exporters) {
		if (exporters == null) {
			exporters = new ArrayList<>();
		}
		Configuration.exporters = new ArrayList<>();
		Iterator<Exporter> exporterIterator = exporters.iterator();
		while (exporterIterator.hasNext()) {
			Exporter exporter = exporterIterator.next();

			if (exporter.getExporter().equals("postgres")) {
				PostgresExporter newExporter = new PostgresExporter(exporter);
				Configuration.exporters.add(newExporter);
			} else if (exporter.getExporter().equals("thingsboard")) {
				ThingsBoardExporter newExporter = new ThingsBoardExporter(exporter);
				Configuration.exporters.add(newExporter);
			} else if (exporter.getExporter().equals("prometheus")) {
				PrometheusExporter newExporter = new PrometheusExporter(exporter);
				Configuration.exporters.add(newExporter);
			} else {
				Exporter newExporter = new Exporter(exporter);
				Configuration.exporters.add(newExporter);
				//System.out.println("Error: Unknown device type: " + exporter.getType());
			}
		}
	}

	public static List<Importer> getImporters() {
		if (importers == null) {
			importers = new ArrayList<>();
		}
		return importers;
	}

	public void setImporters(List<Importer> importers) {
		if (importers == null) {
			importers = new ArrayList<>();
		}
		Configuration.importers = new ArrayList<>();
		Iterator<Importer> importerIterator = importers.iterator();
		while (importerIterator.hasNext()) {
			Importer importer = importerIterator.next();

			if (importer.getImporter().equals("mqtt")) {
				MQTTImporter newImporter = new MQTTImporter(importer);
				Configuration.importers.add(newImporter);
			} else {
				Importer newExporter = new Importer(importer);
				Configuration.importers.add(importer);
				//System.out.println("Error: Unknown device type: " + exporter.getType());
			}
		}
	}


	@Override
    public String toString() {
        return "Order [orderNo=  ]";
    }

}