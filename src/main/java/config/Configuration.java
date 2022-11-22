package config;//package com.baeldung.jackson.yaml; XXXXXX

import agent.Agent;
import agent.LaneAgent;
import device.*;
import exporter.*;
import importer.Importer;
import importer.MQTTImporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

    // Devices settings
	private static List<Device> devices;

	private static List<Agent> agents;

	private static List<Exporter> exporters;

	private static List<Importer> importers;


    public Configuration() {

    }

    public Configuration(String thingsboardMQTThost, String thingsboardMQTTPublishTopic, List<Device> devices, List<Agent> agents, Exporter exporter) {
        super();

        this.devices = devices;
		this.agents = agents;
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
			} else if (device.getType().equals("laneagent")) {
				LaneAgentDevice newDevice = new LaneAgentDevice(device);
				Configuration.devices.add(newDevice);
			} else {
				System.out.println("Error: Unknown device type: " + device.getType());
			}
		}
    }

	public static List<Agent> getAgents() {
		if (agents == null) {
			agents = new ArrayList<>();
		}
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		if (agents == null) {
			agents = new ArrayList<>();
		}
		Configuration.agents = new ArrayList<>();
		Iterator<Agent> agentconfIterator = agents.iterator();
		while (agentconfIterator.hasNext()) {
			Agent agent = agentconfIterator.next();
			if (agent.geType().equals("lane")) {
				LaneAgent newAgent = new LaneAgent(agent);
				Configuration.agents.add(newAgent);
			} else {
				System.out.println("Error: Unknown device type: " + agent.getName());
			}

			//AgentConfiguration newac = new AgentConfiguration(agentConfiguration);
			//this.agents.add(newac);

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