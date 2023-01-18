package config;//package com.baeldung.jackson.yaml; XXXXXX

import CommandReceiver.MQTTProxyReceiver;
import CommandReceiver.MQTTThingsBoardReceiver;
import CommandReceiver.Receiver;
import CommandReceiver.MQTTReceiver;

import agent.Agent;
import agent.LaneAgent;
import device.*;
import emulator.Emulator;
import exporter.*;
import importer.Importer;
import importer.MQTTProxyImporter;
import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {
	private static final Logger LOGGER = LogManager.getLogger(Configuration.class);
	private static List<Device> devices;
	private static List<Agent> agents;
	private static List<Exporter> exporters;
	private static List<Importer> importers;
	private static List<Receiver> receivers;
	private static List<Emulator> emulators;

    public Configuration() {

    }

    public Configuration(String thingsboardMQTThost, String thingsboardMQTTPublishTopic, List<Agent> agents, Exporter exporter) {
        super();

		this.agents = agents;
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
			} else if (exporter.getExporter().equals("mqttwebsocket")) {
				MQTTProxyExporter newExporter = new MQTTProxyExporter(exporter);
				Configuration.exporters.add(newExporter);
			} else {
				LOGGER.info("Error: Unknown exporter type: " + exporter.getExporter());
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

			if (importer.getImporter().equals("shelliesmqtt")) {
				ShelliesMQTTImporter newImporter = new ShelliesMQTTImporter(importer);
				Configuration.importers.add(newImporter);
			} else if (importer.getImporter().equals("mqttimporter")) {
				MQTTProxyImporter newImporter = new MQTTProxyImporter(importer);
				Configuration.importers.add(newImporter);
			} else{
				LOGGER.info("Error: Unknown importer type: " + importer.getImporter());
			}
		}
	}
	public static List<Receiver> getReceivers() {
		if (receivers == null) {
			receivers = new ArrayList<>();
		}
		return receivers;
	}
	public void setReceivers(List<Receiver> receivers) {
		if (receivers == null) {
			receivers = new ArrayList<>();
		}
		Configuration.receivers = new ArrayList<>();
		Iterator<Receiver> importerIterator = receivers.iterator();
		while (importerIterator.hasNext()) {
			Receiver receiver = importerIterator.next();

			if (receiver.getReceiver().equals("mqttreceiver")) {
				MQTTReceiver newReceiver = new MQTTReceiver(receiver);
				Configuration.receivers.add(newReceiver);
			} else if (receiver.getReceiver().equals("mqttproxyreceiver")) {
				MQTTProxyReceiver newReceiver = new MQTTProxyReceiver(receiver);
				Configuration.receivers.add(newReceiver);
			} else if (receiver.getReceiver().equals("mqttthingsboardreceiver")) {
				MQTTThingsBoardReceiver newReceiver = new MQTTThingsBoardReceiver(receiver);
				Configuration.receivers.add(newReceiver);
			} else{
				LOGGER.info("Error: Unknown Receiver.command " + receiver.getReceiver());
			}
		}
	}

	public static List<Emulator> getEmulators() {
		if (emulators == null) {
			emulators = new ArrayList<>();
		}
		return emulators;
	}
	public void setEmulators(List<Emulator> emulators) {
		if (emulators == null) {
			emulators = new ArrayList<>();
		}
		Configuration.emulators = new ArrayList<>();
		Iterator<Emulator> importerIterator = emulators.iterator();
		while (importerIterator.hasNext()) {
			Emulator emulator = importerIterator.next();

			if (emulator.getEmulator().equals("shellyemulator")) {
				Emulator newShellyEmulator = new Emulator(emulator);
				Configuration.emulators.add(newShellyEmulator);
			} /*else if (controller.getController().equals("httpcontroller")) {
				HTTPCommandController newCommandController = new HTTPCommandController(controller);
				Configuration.controllers.add(newCommandController);
			} */else{
				LOGGER.info("Error: Unknown emulator type: " + emulator.getEmulator());
			}
		}
	}


	@Override
    public String toString() {
        return "Order [orderNo=  ]";
    }

}