package exporter;

import importer.Importer;
import importer.MQTTImporterTopicSubscriber;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
//import io.prometheus.client.;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;

public class PrometheusExporter extends Exporter {

    private static final Logger LOGGER = LogManager.getLogger(PrometheusExporter.class);

    public PrometheusExporter(Exporter exporter) {
        this.setName(exporter.getName());
        this.setPort(exporter.getPort());
    }

    static Counter counter = Counter.build().namespace("energydashboard").name("my_counter").help("This is my counter").register();
    static Gauge powerGauge = Gauge.build().namespace("energydashboard").name("power").help("This is power gauge").labelNames("name", "type").register();

    static Gauge statusGauge = Gauge.build().namespace("energydashboard").name("status").help("This is status gauge").labelNames("name", "type").register();
    static Histogram histogram = Histogram.build().namespace("energydashboard").name("my_histogram").help("This is my histogram").register();
    static Summary summary = Summary.build().namespace("energydashboard").name("my_summary").help("This is my summary").register();

    static

    void processRequest() {
        powerGauge.inc();
        // Your code here.
        powerGauge.dec();
    }

    public  void init() {
        try {
            String _port = getPort();
            int pp = Integer.valueOf(getPort());
            HTTPServer server = new HTTPServer(pp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double rand(double min, double max) {
        return min + (Math.random() * (max - min));
    }

    public void publishPowerMetric(String name, String type, String token, LocalDateTime localDateTime, double power) {

        LOGGER.info("Publish powwer metric: name=" + name + " powe=" + power);
        try {
            counter.inc(rand(0, 5));
            powerGauge.labels(name, type).set(power);
            histogram.observe(rand(0, 5));
            summary.observe(rand(0, 5));

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void publishStatusMetric(String name, String type, LocalDateTime localDateTime, double status) {

        LOGGER.info("Publish status metric: name=" + name + " status=" + status);
        try {
            //counter.inc(rand(0, 5));
            statusGauge.labels(name, type).set(status);

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
