package config;//package com.baeldung.jackson.yaml; XXXXXX

import CommandControllers.CommandController;
import CommandControllers.HTTPCommandController;
import CommandControllers.MQTTCommandController;
import agent.Agent;
import agent.LaneAgent;
import device.*;
import exporter.*;
import importer.Importer;
import importer.MQTTImporter;
import importer.ShelliesMQTTImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

	private static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    // device.Devices settings
	private static List<Device> devices;

	private static List<Agent> agents;

	private static List<Exporter> exporters;

	private static List<Importer> importers;

	private static List<CommandController> controllers;


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
				MQTTExporter newExporter = new MQTTExporter(exporter);
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
				MQTTImporter newImporter = new MQTTImporter(importer);
				Configuration.importers.add(newImporter);
			} else{
				LOGGER.info("Error: Unknown importer type: " + importer.getImporter());
			}
		}
	}
	public static List<CommandController> getControllers() {
		if (controllers == null) {
			controllers = new ArrayList<>();
		}
		return controllers;
	}
	public void setControllers(List<CommandController> controllers) {
		if (controllers == null) {
			controllers = new ArrayList<>();
		}
		Configuration.controllers = new ArrayList<>();
		Iterator<CommandController> importerIterator = controllers.iterator();
		while (importerIterator.hasNext()) {
			CommandController controller = importerIterator.next();

			if (controller.getController().equals("mqttcontroller")) {
				MQTTCommandController newCommandController = new MQTTCommandController(controller);
				Configuration.controllers.add(newCommandController);
			} /*else if (controller.getController().equals("httpcontroller")) {
				HTTPCommandController newCommandController = new HTTPCommandController(controller);
				Configuration.controllers.add(newCommandController);
			} */else{
				LOGGER.info("Error: Unknown command controller type: " + controller.getController());
			}
		}
	}


	@Override
    public String toString() {
        return "Order [orderNo=  ]";
    }

}