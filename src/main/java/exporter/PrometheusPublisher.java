package exporter;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.time.LocalDateTime;

public final class PrometheusPublisher {
    static Counter counter = Counter.build().namespace("java").name("my_counter").help("This is my counter").register();
    static Gauge gauge = Gauge.build().namespace("java").name("my_gauge").help("This is my gauge").register();
    static Histogram histogram = Histogram.build().namespace("java").name("my_histogram").help("This is my histogram").register();
    static Summary summary = Summary.build().namespace("java").name("my_summary").help("This is my summary").register();

    public static void init() {

        try {

            HTTPServer server = new HTTPServer(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double rand(double min, double max) {
        return min + (Math.random() * (max - min));
    }

    public static void publishPowerMetric(LocalDateTime localDateTime, double power) {

        try {
            counter.inc(rand(0, 5));
            gauge.set(power);
            histogram.observe(rand(0, 5));
            summary.observe(rand(0, 5));


            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
