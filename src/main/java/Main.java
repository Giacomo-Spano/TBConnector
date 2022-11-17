import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import config.AgentConfiguration;
import config.Configuration;
import exporter.Exporter;
import importer.Importer;
import scheduler.MyScheduler;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // configuring the objectMapper to ignore the error in case we have some
        // unrecognized fields
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        mapper.findAndRegisterModules();
        try {
            Configuration configuration = mapper.readValue(new File("./config/configuration.yaml"), Configuration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AgentConfiguration ag = Configuration.getAgent().get(0);


        MyScheduler myScheduler = new MyScheduler();
        myScheduler.init();

        //PrometheusPublisher.init();
        Iterator<Exporter> exporterIterator = Configuration.getExporters().iterator();
        while (exporterIterator.hasNext()) {
            Exporter exporter = exporterIterator.next();
            exporter.init();
        }

        Iterator<Importer> importerIterator = Configuration.getImporters().iterator();
        while (importerIterator.hasNext()) {
            Importer importer = importerIterator.next();
            importer.init();
        }
    }
}