import agent.Agent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.Configuration;

import exporter.Exporter;
import importer.Importer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import CommandControllers.HTTPCommandApplication;
import scheduler.JobScheduler;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);


    public static void main(String[] args) {

        LOGGER.info("");
        LOGGER.info("** VERSION .082");
        LOGGER.info("");
        LOGGER.debug("This is a debug message");
        LOGGER.info("This is an info message");
        LOGGER.warn("This is a warn message");
        LOGGER.error("This is an error message");
        LOGGER.fatal("This is a fatal message");

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // configuring the objectMapper to ignore the error in case we have some
        // unrecognized fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        LOGGER.info("Working Directory = " + System.getProperty("user.dir"));

        mapper.findAndRegisterModules();
        try {
            Configuration configuration = mapper.readValue(new File("./config/configuration.yaml"), Configuration.class);
        } catch (IOException e) {
            LOGGER.fatal("error reading configuration.yaml " + e.toString() + " " );
            throw new RuntimeException(e);
        }

        //Command cmd = new ThingsBoardCommandController();

        // start agent
        if (Configuration.getAgents() != null && Configuration.getAgents().size() > 0) {
            //Agent ag = Configuration.getAgents().get(0);
            Iterator<Agent> agentIterator = Configuration.getAgents().iterator();
            while (agentIterator.hasNext()) {
                Agent agent = agentIterator.next();
                agent.init();
            }
        }

        // start scheduler
        JobScheduler jobScheduler = new JobScheduler();
        jobScheduler.init();


        // start importer
        if (Configuration.getExporters() != null && Configuration.getExporters().size() > 0) {
            Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
            while (exporterIterator.hasNext()) {
                Exporter exporter = exporterIterator.next();
                exporter.init();
            }
        }

        //start exporter
        if (Configuration.getImporters() != null && Configuration.getImporters().size() > 0) {
            Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
            while (importerIterator.hasNext()) {
                Importer importer = importerIterator.next();
                importer.init();
            }
        }

        SpringApplication.run(HTTPCommandApplication.class, args);



        //MyKafkaProducer s = new MyKafkaProducer();

        //s.sendMessage("aa", "bb");
        //_MQTTWSTopicPublisher tp = new _MQTTWSTopicPublisher();
        //tp.prova();


    }


}