package config;//package com.baeldung.jackson.yaml; XXXXXX

import CommandControllers.CommandController;
import agent.Agent;
import agent.LaneAgent;
import device.*;
import exporter.*;
import importer.Importer;
import importer.ShelliesMQTTImporter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {

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

			if (importer.getImporter().equals("shelliesmqtt")) {
				ShelliesMQTTImporter newImporter = new ShelliesMQTTImporter(importer);
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