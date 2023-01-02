package exporter;


import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;

public class PrometheusExporter extends Exporter {

    private static final Logger LOGGER = LogManager.getLogger(PrometheusExporter.class);

    public PrometheusExporter(Exporter exporter) {
        this.setName(exporter.getName());
        this.setPort(exporter.getPort());
        this.setNamespace(exporter.getNamespace());

        powerGauge  = Gauge.build().namespace(getNamespace()).name("power").help("This is power gauge").labelNames("name", "type").register();
        counter = Counter.build().namespace(getNamespace()).name("my_counter").help("This is my counter").register();
        statusGauge = Gauge.build().namespace(getNamespace()).name("status").help("This is status gauge").labelNames("name", "type").register();
        histogram = Histogram.build().namespace(getNamespace()).name("my_histogram").help("This is my histogram").register();
        summary = Summary.build().namespace(getNamespace()).name("my_summary").help("This is my summary").register();

    }

    private Counter counter;// = Counter.build().namespace(getNamespace()).name("my_counter").help("This is my counter").register();
    private Gauge powerGauge;//  = Gauge.build().namespace("energydashboard").name("power").help("This is power gauge").labelNames("name", "type").register();
    private Gauge statusGauge;//  = Gauge.build().namespace("energydashboard").name("status").help("This is status gauge").labelNames("name", "type").register();
    private Histogram histogram;//  = Histogram.build().namespace("energydashboard").name("my_histogram").help("This is my histogram").register();
    Summary summary;//  = Summary.build().namespace("energydashboard").name("my_summary").help("This is my summary").register();

    /*static void processRequest() {
        powerGauge.inc();
        // Your code here.
        powerGauge.dec();
    }*/

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

    public void publishTelemetry(JSONObject json) {
        LOGGER.info("publishPowerMetric.");
        String name = json.getString("name");
        String type = json.getString("type");
        Double power = json.getDouble("power");
        LOGGER.info("publishPowerMetric. name: " + name);
        LOGGER.info("publishPowerMetric. type " + type);
        LOGGER.info("publishPowerMetric. power " + power);

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
    public void publishTelemetry(String name, String type, String token, LocalDateTime localDateTime, double power) {

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
